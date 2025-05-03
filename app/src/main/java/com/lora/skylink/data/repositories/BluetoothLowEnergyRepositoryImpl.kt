package com.lora.skylink.data.repositories

import com.lora.skylink.data.framework.bluetooth.scanning.BluetoothLowEnergyScanController
import com.lora.skylink.data.framework.bluetooth.scanning.BluetoothReadyCheckerImpl
import com.lora.skylink.data.model.WirelessDevice
import com.lora.skylink.domain.interfaces.IBluetoothLowEnergyRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class BluetoothLowEnergyRepositoryImpl @Inject constructor(
    private val bleScanController: BluetoothLowEnergyScanController,
    private val bluetoothReadyChecker: BluetoothReadyCheckerImpl
) : IBluetoothLowEnergyRepository {

    override val scannedDevices: StateFlow<List<WirelessDevice>> = bleScanController.scannedDevices
    override fun startBleScan() = bleScanController.startBleScan()
    override fun stopBleScan() = bleScanController.stopBleScan()
    override fun isBluetoothAdapterReady():Boolean = bluetoothReadyChecker.isBluetoothAdapterReady()

}
