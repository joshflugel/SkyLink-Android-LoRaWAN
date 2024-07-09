package com.lora.skylink.data.remote.bluetooth

import android.bluetooth.BluetoothAdapter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BluetoothAdapterManager @Inject constructor(
    private val bluetoothAdapter: BluetoothAdapter
) {
    fun isBluetoothAdapterEnabled(): Boolean {
        return bluetoothAdapter.isEnabled
    }
}
