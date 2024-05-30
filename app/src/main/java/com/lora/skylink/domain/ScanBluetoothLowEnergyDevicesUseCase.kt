package com.lora.skylink.domain

import android.bluetooth.le.ScanResult
import com.lora.skylink.data.BluetoothLowEnergyRepositoryImpl
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ScanBluetoothLowEnergyDevicesUseCase @Inject constructor(
    private val repository: BluetoothLowEnergyRepositoryImpl
) {
    fun startScanning() {
        println("FLUGEL UseCase SCAN BLE")
     repository.startBleScan()
    }
    fun stopScanning() = repository.stopBleScan()
    operator fun invoke(): StateFlow<List<ScanResult>> {
        return repository.scannedDevices
    }
}
