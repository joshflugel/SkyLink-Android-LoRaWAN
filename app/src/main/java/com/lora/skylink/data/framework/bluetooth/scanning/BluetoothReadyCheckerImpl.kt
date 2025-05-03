package com.lora.skylink.data.framework.bluetooth.scanning

import android.bluetooth.BluetoothAdapter
import com.lora.skylink.domain.interfaces.BluetoothReadyChecker
import javax.inject.Inject

class BluetoothReadyCheckerImpl @Inject constructor(
    private val bluetoothAdapter: BluetoothAdapter
) : BluetoothReadyChecker {

    override fun isBluetoothAdapterReady(): Boolean {
        return bluetoothAdapter.isEnabled
    }
}
