package com.lora.skylink.data.framework.bluetooth.scanning

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.os.Build
import androidx.annotation.RequiresApi
import com.lora.skylink.data.model.WirelessDevice
import com.lora.skylink.util.toWirelessDevice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BluetoothLowEnergyScanController @Inject constructor(
    private val bluetoothAdapter: BluetoothAdapter
) {

    private val _scannedDevices = MutableStateFlow<List<WirelessDevice>>(emptyList())
    val scannedDevices: StateFlow<List<WirelessDevice>> = _scannedDevices

    private val bleScanner by lazy {
        bluetoothAdapter.bluetoothLeScanner
    }

    private val scanCallback = object : ScanCallback() {
        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val currentList = _scannedDevices.value.toMutableList()
            val indexQuery = currentList.indexOfFirst { it.macAddress == result.device.address }
            // Only show Connectable devices, ignore the busy ones
            if (indexQuery != -1) {result.isConnectable
                currentList[indexQuery] = result.toWirelessDevice()
            } else {
                with(result.device) {
                    if(name?.startsWith("LORA")==true) {
                        println("FLUGEL Found BLE ARDUINO device! Name: ${name ?: "Unnamed"}, address: $address")
                        println("FLUGEL - isConnectable: ${result.isConnectable}")
                        currentList.add(result.toWirelessDevice())
                    }
                }
            }
            _scannedDevices.value = currentList
        }

        override fun onScanFailed(errorCode: Int) {
            println("FLUGEL - SCAN FAILURE")
        }
    }


    private val scanSettings: ScanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
        .build()

    @SuppressLint("MissingPermission")
    fun startBleScan() {
        bleScanner.startScan(null, scanSettings, scanCallback)
    }

    @SuppressLint("MissingPermission")
    fun stopBleScan() {
        bleScanner.stopScan(scanCallback)
    }
}