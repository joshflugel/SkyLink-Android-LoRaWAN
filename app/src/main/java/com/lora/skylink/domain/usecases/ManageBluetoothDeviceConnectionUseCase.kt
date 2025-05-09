package com.lora.skylink.domain.usecases

import com.lora.skylink.data.framework.bluetooth.communication.ConnectionEventListener
import com.lora.skylink.data.model.WirelessDevice
import com.lora.skylink.domain.interfaces.IBluetoothConnectivityRepository
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