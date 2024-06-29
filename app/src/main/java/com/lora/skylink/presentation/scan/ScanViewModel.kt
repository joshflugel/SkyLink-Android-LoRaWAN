package com.lora.skylink.presentation.scan

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
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
    val uiState: StateFlow<ScanUIState> = _uiState

    val isBluetoothEnabled = MutableLiveData<Boolean>()

    private var scanJob: Job? = null
    fun startScanning() {
        scanJob?.cancel()
        scanJob = viewModelScope.launch {
            clearDeviceList()
            _uiState.update { it.copy(isScanning = true) }
            scanBluetoothLeDevicesUseCase.startScanning()
            val defaultScanSamplingMilliseconds = 1000L
            scanBluetoothLeDevicesUseCase().sample(defaultScanSamplingMilliseconds).collect { devices ->
                updateScannedDevices(devices)
            }
        }
    }

    private fun updateScannedDevices(newDevices: List<ScanResult>) {
        _uiState.update { it.copy(scannedDevices = newDevices) }
    }

    fun stopScanning() {
        scanJob?.cancel()
        viewModelScope.launch {
            scanBluetoothLeDevicesUseCase.stopScanning()
            _uiState.update { it.copy(isScanning = false) }
            clearDeviceList()
        }
    }

    fun clearDeviceList() {
        _uiState.update { it.copy(scannedDevices = emptyList()) }
        //_uiState = MutableStateFlow(ScanUIState()) ToDo
    }

    fun showEnableBluetoothPromptIfDisabled() {
        viewModelScope.launch {
            isBluetoothEnabled.value = checkBluetoothEnabledUseCase()
        }
    }

    public fun clearViewModel() {
        onCleared()
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