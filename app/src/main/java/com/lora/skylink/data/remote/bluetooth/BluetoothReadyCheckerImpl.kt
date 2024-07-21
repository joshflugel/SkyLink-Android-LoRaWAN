package com.lora.skylink.data.remote.bluetooth

import android.bluetooth.BluetoothAdapter
import com.lora.skylink.domain.BluetoothReadyChecker
import javax.inject.Inject

class BluetoothReadyCheckerImpl @Inject constructor(
    private val bluetoothAdapter: BluetoothAdapter
) : BluetoothReadyChecker {

    override fun isBluetoothAdapterReady(): Boolean {
        return bluetoothAdapter.isEnabled
    }
}
