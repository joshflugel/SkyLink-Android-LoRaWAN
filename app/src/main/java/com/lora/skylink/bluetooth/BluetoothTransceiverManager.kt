package com.lora.skylink.bluetooth

import android.bluetooth.le.ScanSettings

class BluetoothTransceiverManager {
    val scanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .build()


}