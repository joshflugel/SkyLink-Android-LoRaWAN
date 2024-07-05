package com.lora.skylink.data.model

data class WirelessDevice (
    val name: String?,
    val macAddress: String,
    val signalStrength_dBm: Int
)