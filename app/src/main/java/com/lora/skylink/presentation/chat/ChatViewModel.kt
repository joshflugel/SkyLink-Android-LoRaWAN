package com.lora.skylink.presentation.chat

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lora.skylink.bluetoothlegacy.ConnectionEventListener
import com.lora.skylink.domain.ManageBluetoothDeviceConnectionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val manageBluetoothDeviceConnectionUseCase: ManageBluetoothDeviceConnectionUseCase
): ViewModel() {

    // TODO: Implement the ViewModel

    fun disconnectFromDevice(device: BluetoothDevice) {
        viewModelScope.launch { manageBluetoothDeviceConnectionUseCase.disconnectFromDevice(device) }
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