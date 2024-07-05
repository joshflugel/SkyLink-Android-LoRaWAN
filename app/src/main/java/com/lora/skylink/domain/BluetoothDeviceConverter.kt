package com.lora.skylink.domain

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import com.lora.skylink.data.model.WirelessDevice

interface BluetoothDeviceConverter {
    fun toBluetoothDevice(device: WirelessDevice): BluetoothDevice?
}

class BluetoothDeviceConverterImpl(
    private val bluetoothAdapter: BluetoothAdapter
) : BluetoothDeviceConverter {
    override fun toBluetoothDevice(device: WirelessDevice): BluetoothDevice? {
        return bluetoothAdapter.getRemoteDevice(device.macAddress)
    }
}