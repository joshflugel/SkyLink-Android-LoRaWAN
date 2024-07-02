package com.lora.skylink.presentation.scan

import androidx.fragment.app.Fragment
import com.lora.skylink.data.model.WirelessDevice

fun Fragment.buildScanUIState(
    scannedDevices: List<WirelessDevice> = emptyList(),
    isScanning: Boolean = false
) = ScanUIState(
    scannedDevices = scannedDevices,
    isScanning = isScanning
)

data class ScanUIState (
    val scannedDevices: List<WirelessDevice> = emptyList(),
    val isScanning: Boolean = false
)
