package com.lora.skylink.presentation.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.multidex.BuildConfig
import com.lora.skylink.data.model.WirelessDevice
import com.lora.skylink.data.framework.bluetooth.communication.ConnectionEventListener
import com.lora.skylink.domain.usecases.CheckBluetoothEnabledUseCase
import com.lora.skylink.domain.usecases.ManageBluetoothDeviceConnectionUseCase
import com.lora.skylink.domain.usecases.ScanBluetoothLowEnergyDevicesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val scanBluetoothLeDevicesUseCase: ScanBluetoothLowEnergyDevicesUseCase,
    private val checkBluetoothEnabledUseCase: CheckBluetoothEnabledUseCase,
    private val manageBluetoothDeviceConnectionUseCase: ManageBluetoothDeviceConnectionUseCase
) : ViewModel() {

    private var _uiState = MutableStateFlow(ScanUIState())
    val uiState = _uiState.asStateFlow()
    private var scanJob: Job? = null

    private val _eventFlow = MutableSharedFlow<ScanUIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val connectionEventListener by lazy {
        ConnectionEventListener().apply {
            onConnectionSetupComplete = { gatt ->
                viewModelScope.launch {
                    loge("Emitting NavigateToChat event... ${gatt.device.name}")
                    _eventFlow.emit(ScanUIEvent.NavigateToChat(gatt.device))
                }
            }
            onDisconnect = {
                loge("ScanFrag ...DISCONNECTED from Bluetooth LoRa Arduino")
            }
        }
    }



    fun btnScanDevicesPressed() {
        showEnableWirelessDevicePromptIfDisabled()
        if (uiState.value.isScanning) {
            stopScanning()
        } else {
            startScanning()
        }
    }

    fun startScanning() {
        scanJob?.cancel()
        scanJob = viewModelScope.launch {
            _uiState.update { it.copy(isScanning = true, scannedDevices = emptyList()) }
            scanBluetoothLeDevicesUseCase.startScanning()
            val defaultScanSamplingMilliseconds = 600L

            scanBluetoothLeDevicesUseCase().sample(defaultScanSamplingMilliseconds).collect { devices ->
                _uiState.update { it.copy(scannedDevices = devices) }
            }
        }
    }

    fun stopScanning() {
        scanJob?.cancel()

        viewModelScope.launch {
            scanBluetoothLeDevicesUseCase.stopScanning()
            _uiState.update { it.copy(isScanning = false, scannedDevices = emptyList()) }
        }
    }

    fun showEnableWirelessDevicePromptIfDisabled() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isWirelessDeviceEnabled = checkBluetoothEnabledUseCase())
        }
    }

    fun connectToDevice(device: WirelessDevice) {
        stopScanning()
        viewModelScope.launch {
            try {
                manageBluetoothDeviceConnectionUseCase.connectToDevice(device)
            } catch (e: Exception) {
                _eventFlow.emit(ScanUIEvent.ShowError(e.message ?: "Unknown error"))
            }
        }
    }

    fun registerConnectionEventListener() {
        viewModelScope.launch {
            manageBluetoothDeviceConnectionUseCase.registerConnectionEventListener(connectionEventListener)
        }
    }

    fun unregisterConnectionEventListener() {
        viewModelScope.launch {
            manageBluetoothDeviceConnectionUseCase.unregisterConnectionEventListener(connectionEventListener)
        }
    }

    fun printScannedDevicesInLogcat(devices: List<WirelessDevice>) {
        val devicesString = devices.joinToString(separator = "\n") { device ->
            "Name: ${device.name}, MAC Address: ${device.macAddress}, Signal Strength: ${device.signalStrength_dBm}"
        }
        loge("List of Devices:\n$devicesString")
    }

}