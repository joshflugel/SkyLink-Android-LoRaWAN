package com.lora.skylink.domain

typealias ScannedBluetoothDeviceDomain = ScanResultBK
data class ScanResultBK(
    val name: String?,
    val address: String
)