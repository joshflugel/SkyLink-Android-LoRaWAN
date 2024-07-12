package com.lora.skylink.domain

import com.lora.skylink.data.remote.bluetoothlowenergy.ConnectionEventListener
import com.lora.skylink.data.model.WirelessDevice
import javax.inject.Inject

class ManageBluetoothDeviceConnectionUseCase @Inject constructor(
    private val bluetoothConnectivityRepository: IBluetoothConnectivityRepository
) {
    fun connectToDevice(device: WirelessDevice) {
        bluetoothConnectivityRepository.connectToDevice(device)
    }

    fun disconnectFromDevice(device: WirelessDevice) {
        bluetoothConnectivityRepository.disconnectFromDevice(device)
    }

    fun registerConnectionEventListener(listener: ConnectionEventListener) {
        bluetoothConnectivityRepository.registerConnectionEventListener(listener)
    }

    fun unregisterConnectionEventListener(listener: ConnectionEventListener) {
        bluetoothConnectivityRepository.unregisterConnectionEventListener(listener)
    }
}