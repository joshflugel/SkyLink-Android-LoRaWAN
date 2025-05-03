package com.lora.skylink.domain.usecases

import com.lora.skylink.data.model.WirelessDevice
import com.lora.skylink.data.repositories.BluetoothLowEnergyRepositoryImpl
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ScanBluetoothLowEnergyDevicesUseCase @Inject constructor(
    private val repository: BluetoothLowEnergyRepositoryImpl
) {
    fun startScanning() {
     repository.startBleScan()
    }
    fun stopScanning() = repository.stopBleScan()
    operator fun invoke(): StateFlow<List<WirelessDevice>> {
        return repository.scannedDevices
    }
}
