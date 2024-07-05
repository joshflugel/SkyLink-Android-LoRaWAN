package com.lora.skylink.presentation.scan

import androidx.fragment.app.Fragment
import com.lora.skylink.data.model.WirelessDevice

data class ScanUIState (
    val scannedDevices: List<WirelessDevice> = emptyList(),
    val isScanning: Boolean = false
)
