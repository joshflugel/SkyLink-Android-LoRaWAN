package com.lora.skylink.domain

import com.lora.skylink.data.model.WirelessDevice
import kotlinx.coroutines.flow.StateFlow

interface IBluetoothLowEnergyRepository {

    val scannedDevices: StateFlow<List<WirelessDevice>>
    fun startBleScan()
    fun stopBleScan()
    fun isBluetoothAdapterEnabled(): Boolean
}