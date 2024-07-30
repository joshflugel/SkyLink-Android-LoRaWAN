package com.lora.skylink.presentation.scan

import androidx.lifecycle.MutableLiveData
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
import kotlinx.coroutines.flow.StateFlow
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

    private val _uiState = MutableStateFlow<ScanUIState>(ScanUIState.Idle)
    val uiState: StateFlow<ScanUIState> = _uiState.asStateFlow()

    val isWirelessDeviceEnabled = MutableLiveData<Boolean>()

    private val _scannedDevices = MutableStateFlow<List<WirelessDevice>>(emptyList())
    val scannedDevices: StateFlow<List<WirelessDevice>> = _scannedDevices.asStateFlow()


    private var scanJob: Job? = null
    fun startScanning() {
        scanJob?.cancel()
        scanJob = viewModelScope.launch {
            _uiState.update { ScanUIState.Scanning}
            scanBluetoothLeDevicesUseCase.startScanning()
            val defaultScanSamplingMilliseconds = 1000L

            scanBluetoothLeDevicesUseCase().sample(defaultScanSamplingMilliseconds).collect { devices ->
                _scannedDevices.update { devices }
            }
        }
    }

    fun stopScanning() {
        scanJob?.cancel()

        viewModelScope.launch {
            scanBluetoothLeDevicesUseCase.stopScanning()
            _uiState.update { ScanUIState.Idle }
        }
    }

    fun showEnableWirelessDevicePromptIfDisabled() {
        viewModelScope.launch {
            isWirelessDeviceEnabled.value = checkBluetoothEnabledUseCase()
        }
    }

    fun connectToDevice(device: WirelessDevice) {
        stopScanning()
        viewModelScope.launch {
            try {
                manageBluetoothDeviceConnectionUseCase.connectToDevice(device)
                _uiState.update { ScanUIState.Idle}
            } catch (e: Exception) {
                _uiState.update { ScanUIState.Error(e.message ?: "Unknown Connection error")}
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

    fun btnScanDevicesPressed() {
        when (_uiState.value) {
            is ScanUIState.Scanning -> stopScanning()
            else -> startScanning()
        }
    }

}