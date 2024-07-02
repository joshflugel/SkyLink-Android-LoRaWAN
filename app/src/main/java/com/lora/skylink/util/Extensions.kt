package com.lora.skylink.util

import android.annotation.SuppressLint
import android.bluetooth.le.ScanResult
import com.lora.skylink.data.model.WirelessDevice
@SuppressLint("MissingPermission")
fun ScanResult.toWirelessDevice(): WirelessDevice {
    return WirelessDevice(
        name = this.device.name,
        macAddress = this.device.address,
        signalStrength_dBm = this.rssi
    )
}