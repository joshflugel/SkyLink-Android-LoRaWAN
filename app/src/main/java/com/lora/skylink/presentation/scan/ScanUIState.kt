package com.lora.skylink.presentation.scan

import com.lora.skylink.data.model.WirelessDevice

data class ScanUIState (
    val scannedDevices: List<WirelessDevice> = emptyList(),
    val isScanning: Boolean = false,
    val isWirelessDeviceEnabled: Boolean = true,
    val errorMessage: String = String()
)
