package com.lora.skylink.domain.interfaces

import com.lora.skylink.data.framework.bluetooth.communication.ConnectionEventListener
import com.lora.skylink.data.model.WirelessDevice

interface IBluetoothConnectivityRepository {
        fun connectToDevice(device: WirelessDevice)
        fun disconnectFromDevice(device: WirelessDevice)
        fun teardownConnection(device: WirelessDevice)
        fun registerConnectionEventListener(listener: ConnectionEventListener)
        fun unregisterConnectionEventListener(listener: ConnectionEventListener)
    }

