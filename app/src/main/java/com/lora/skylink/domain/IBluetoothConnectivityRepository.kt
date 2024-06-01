package com.lora.skylink.domain

import android.bluetooth.BluetoothDevice
import com.lora.skylink.bluetoothlegacy.ConnectionEventListener

interface IBluetoothConnectivityRepository {
        fun connectToDevice(device: BluetoothDevice)
        fun disconnectFromDevice(device: BluetoothDevice)
        fun registerConnectionEventListener(listener: ConnectionEventListener)
        fun unregisterConnectionEventListener(listener: ConnectionEventListener)
    }

