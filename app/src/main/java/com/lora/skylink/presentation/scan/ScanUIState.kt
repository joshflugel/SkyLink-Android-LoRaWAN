package com.lora.skylink.presentation.scan

import android.bluetooth.le.ScanResult
import androidx.fragment.app.Fragment

fun Fragment.buildScanUIState(
    scannedDevices: List<ScanResult> = emptyList(),
    isScanning: Boolean = false
) = ScanUIState(
    scannedDevices = scannedDevices,
    isScanning = isScanning
)

data class ScanUIState (
    val scannedDevices: List<ScanResult> = emptyList(),
    val isScanning: Boolean = false
)
