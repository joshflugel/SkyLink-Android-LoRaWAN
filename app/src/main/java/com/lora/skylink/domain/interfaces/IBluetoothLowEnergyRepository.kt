package com.lora.skylink.domain.interfaces

import com.lora.skylink.data.model.WirelessDevice
import kotlinx.coroutines.flow.StateFlow

interface IBluetoothLowEnergyRepository {

    val scannedDevices: StateFlow<List<WirelessDevice>>
    fun startBleScan()
    fun stopBleScan()
    fun isBluetoothAdapterReady(): Boolean
}