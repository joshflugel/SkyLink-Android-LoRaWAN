package com.lora.skylink.data

import android.annotation.SuppressLint
import android.bluetooth.le.ScanResult
import com.lora.skylink.domain.ScannedBluetoothDeviceDomain

@SuppressLint("MissingPermission")
fun  ScanResult.toScannedBluetoothDeviceDomain(): ScannedBluetoothDeviceDomain {
    return ScannedBluetoothDeviceDomain(
        name = device.name,
        address = device.address
    )
}