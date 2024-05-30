package com.lora.skylink.domain

import android.bluetooth.le.ScanResult
import kotlinx.coroutines.flow.StateFlow

interface IBluetoothLowEnergyRepository {

    val scannedDevices: StateFlow<List<ScanResult>>
    fun startBleScan()
    fun stopBleScan()
    fun isBluetoothAdapterEnabled(): Boolean
}