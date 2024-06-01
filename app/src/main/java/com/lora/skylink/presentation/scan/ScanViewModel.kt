package com.lora.skylink.presentation.scan

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lora.skylink.bluetoothlegacy.ConnectionEventListener
import com.lora.skylink.data.BluetoothConnectivityRepositoryImpl
import com.lora.skylink.domain.CheckBluetoothEnabledUseCase
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
    private val bluetoothConnectivityRepository: BluetoothConnectivityRepositoryImpl,
    private val scanBluetoothLeDevicesUseCase: ScanBluetoothLowEnergyDevicesUseCase,
    private val checkBluetoothEnabledUseCase: CheckBluetoothEnabledUseCase
) : ViewModel() {

    private val _scannedDevices = MutableStateFlow<List<ScanResult>>(emptyList())
    val scannedDevices: StateFlow<List<ScanResult>> = _scannedDevices

    private val _uiState = MutableStateFlow(ScanUIState())
    val uiState: StateFlow<ScanUIState> = _uiState

    private val _isBluetoothEnabled = MutableLiveData<Boolean>()
    val isBluetoothEnabled: LiveData<Boolean> get() = _isBluetoothEnabled

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

    fun checkBluetoothEnabled() {
        viewModelScope.launch {
            _isBluetoothEnabled.value = checkBluetoothEnabledUseCase.isBluetoothEnabled()
        }
    }

    fun connectToDevice(device: BluetoothDevice) {
        bluetoothConnectivityRepository.connectToDevice(device)
    }

    fun disconnectFromDevice(device: BluetoothDevice) {
        bluetoothConnectivityRepository.disconnectFromDevice(device)
    }

    fun registerConnectionEventListener(listener: ConnectionEventListener) {
        bluetoothConnectivityRepository.registerConnectionEventListener(listener)
    }

    fun unregisterConnectionEventListener(listener: ConnectionEventListener) {
        bluetoothConnectivityRepository.unregisterConnectionEventListener(listener)
    }
}