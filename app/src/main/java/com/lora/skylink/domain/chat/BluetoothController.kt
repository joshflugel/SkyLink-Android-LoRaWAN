package com.lora.skylink.domain.chat

import android.bluetooth.le.ScanResult
import kotlinx.coroutines.flow.StateFlow

interface BluetoothController {
    val scannedDevices: StateFlow<List<ScanResult>>

    fun startScanning()
    fun stopScanning()
    fun release()
}