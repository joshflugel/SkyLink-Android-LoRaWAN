package com.lora.skylink.presentation.scan

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lora.skylink.bluetoothlegacy.ConnectionEventListener
import com.lora.skylink.domain.CheckBluetoothEnabledUseCase
import com.lora.skylink.domain.ManageBluetoothDeviceConnectionUseCase
import com.lora.skylink.domain.ScanBluetoothLowEnergyDevicesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val scanBluetoothLeDevicesUseCase: ScanBluetoothLowEnergyDevicesUseCase,
    private val checkBluetoothEnabledUseCase: CheckBluetoothEnabledUseCase,
    private val manageBluetoothDeviceConnectionUseCase: ManageBluetoothDeviceConnectionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScanUIState())
    val uiState: StateFlow<ScanUIState> = _uiState

    private val _isBluetoothEnabled = MutableLiveData<Boolean>()

    private var scanJob: Job? = null
    fun startScanning() {
        scanJob?.cancel()
        scanJob = viewModelScope.launch {
            _uiState.update { it.copy(isScanning = true) }
            scanBluetoothLeDevicesUseCase.startScanning()
            scanBluetoothLeDevicesUseCase().collect { devices ->
                _uiState.update { it.copy(scannedDevices = devices) }
            }
        }
    }

    fun stopScanning() {
        scanJob?.cancel()
        viewModelScope.launch {
            scanBluetoothLeDevicesUseCase.stopScanning()
            _uiState.update { it.copy(isScanning = false) }
        }
    }

    fun showEnableBluetoothPromptIfDisabled() {
        viewModelScope.launch {
            _isBluetoothEnabled.value = checkBluetoothEnabledUseCase()
        }
    }

    fun connectToDevice(device: BluetoothDevice) {
        viewModelScope.launch { manageBluetoothDeviceConnectionUseCase.connectToDevice(device) }
    }

    fun registerConnectionEventListener(listener: ConnectionEventListener) {
        viewModelScope.launch {manageBluetoothDeviceConnectionUseCase.registerConnectionEventListener(listener) }
    }

    fun unregisterConnectionEventListener(listener: ConnectionEventListener) {
        viewModelScope.launch {manageBluetoothDeviceConnectionUseCase.unregisterConnectionEventListener(listener) }
    }

}