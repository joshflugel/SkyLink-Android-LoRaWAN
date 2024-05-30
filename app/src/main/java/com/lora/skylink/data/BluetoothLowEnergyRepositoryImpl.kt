package com.lora.skylink.data

import android.bluetooth.le.ScanResult
import com.lora.skylink.data.bluetoothLowEnergy.BluetoothAdapterManager
import com.lora.skylink.data.bluetoothLowEnergy.BluetoothLowEnergyScanController
import com.lora.skylink.domain.IBluetoothLowEnergyRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class BluetoothLowEnergyRepositoryImpl @Inject constructor(
    private val bleScanController: BluetoothLowEnergyScanController,
    private val bluetoothAdapterManager: BluetoothAdapterManager
) : IBluetoothLowEnergyRepository {

    override val scannedDevices: StateFlow<List<ScanResult>> = bleScanController.scannedDevices
    override fun startBleScan() = bleScanController.startBleScan()
    override fun stopBleScan() = bleScanController.stopBleScan()
    override fun isBluetoothAdapterEnabled():Boolean = bluetoothAdapterManager.isBluetoothAdapterEnabled()

}
