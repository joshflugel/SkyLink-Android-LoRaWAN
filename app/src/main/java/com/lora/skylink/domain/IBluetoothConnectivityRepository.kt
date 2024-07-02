package com.lora.skylink.domain

import com.lora.skylink.bluetoothlegacy.ConnectionEventListener
import com.lora.skylink.data.model.WirelessDevice

interface IBluetoothConnectivityRepository {
        fun connectToDevice(device: WirelessDevice)
        fun disconnectFromDevice(device: WirelessDevice)
        fun teardownConnection(device: WirelessDevice)
        fun registerConnectionEventListener(listener: ConnectionEventListener)
        fun unregisterConnectionEventListener(listener: ConnectionEventListener)
    }

