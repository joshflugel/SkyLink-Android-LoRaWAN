package com.lora.skylink.presentation.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lora.skylink.data.model.WirelessDevice
import com.lora.skylink.data.remote.bluetoothlowenergy.ConnectionEventListener
import com.lora.skylink.domain.CheckBluetoothEnabledUseCase
import com.lora.skylink.domain.ManageBluetoothDeviceConnectionUseCase
import com.lora.skylink.domain.ScanBluetoothLowEnergyDevicesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
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
                _uiState.update { ScanUIState(errorMessage = e.toString()) }
            }
        }
    }

    fun registerConnectionEventListener(listener: ConnectionEventListener) {
        viewModelScope.launch {
            manageBluetoothDeviceConnectionUseCase.registerConnectionEventListener(listener)
        }
    }

    fun unregisterConnectionEventListener(listener: ConnectionEventListener) {
        viewModelScope.launch {
            manageBluetoothDeviceConnectionUseCase.unregisterConnectionEventListener(listener)
        }
    }

}