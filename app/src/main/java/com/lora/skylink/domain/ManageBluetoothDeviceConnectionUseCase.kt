package com.lora.skylink.domain

import android.bluetooth.BluetoothDevice
import com.lora.skylink.bluetoothlegacy.ConnectionEventListener
import com.lora.skylink.data.BluetoothConnectivityRepositoryImpl
import javax.inject.Inject

class ManageBluetoothDeviceConnectionUseCase @Inject constructor(
    private val bluetoothConnectivityRepository: BluetoothConnectivityRepositoryImpl
) {
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