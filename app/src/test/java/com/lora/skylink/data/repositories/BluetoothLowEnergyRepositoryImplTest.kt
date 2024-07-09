package com.lora.skylink.data.repositories

import com.lora.skylink.data.model.WirelessDevice
import com.lora.skylink.data.remote.bluetooth.BluetoothAdapterManager
import com.lora.skylink.data.remote.bluetooth.BluetoothLowEnergyScanController
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.justRun
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Before
import org.junit.Test

class BluetoothLowEnergyRepositoryImplTest {

    @MockK
    private lateinit var bleScanController: BluetoothLowEnergyScanController

    @MockK
    private lateinit var  bluetoothAdapterManager: BluetoothAdapterManager

    private lateinit var bluetoothLowEnergyRepository: BluetoothLowEnergyRepositoryImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        val device1 = WirelessDevice(name = "Device1", macAddress = "00:11:22:33:44:55", signalStrength_dBm = -50)
        val device2 = WirelessDevice(name = "Device2", macAddress = "66:77:88:99:AA:BB", signalStrength_dBm = -60)

        val scannedDevicesFlow = MutableStateFlow<List<WirelessDevice>>(listOf(device1, device2))
        every { bleScanController.scannedDevices } returns scannedDevicesFlow

        bluetoothLowEnergyRepository = BluetoothLowEnergyRepositoryImpl(bleScanController, bluetoothAdapterManager)
    }

    @Test
    fun isBluetoothAdapterEnabledShouldReturnTrueWhenAdapterIsEnabled() {
        every { bluetoothAdapterManager.isBluetoothAdapterEnabled() } returns true

        val result = bluetoothLowEnergyRepository.isBluetoothAdapterEnabled()

        assert(result)
        verify { bluetoothAdapterManager.isBluetoothAdapterEnabled()}
    }

    @Test
    fun isBluetoothAdapterEnabledShouldReturnFalseWhenAdapterIsDisabled() {
        every { bluetoothAdapterManager.isBluetoothAdapterEnabled() } returns false

        val result = bluetoothLowEnergyRepository.isBluetoothAdapterEnabled()

        assert(!result)
        verify { bluetoothAdapterManager.isBluetoothAdapterEnabled() }
    }

    @Test
    fun startBleScanShouldCallStartBleScanFromController() {
        justRun { bleScanController.startBleScan() }

        bluetoothLowEnergyRepository.startBleScan()

        verify { bleScanController.startBleScan() }
    }

    @Test
    fun `stopBleScan should call stopBleScan on bleScanController`() {
        justRun { bleScanController.stopBleScan() }

        bluetoothLowEnergyRepository.stopBleScan()

        verify { bleScanController.stopBleScan() }
    }

    @Test
    fun scannedDevicesShouldReturnScannedDevicesFromController() {
        val device1 = WirelessDevice(name = "Device1", macAddress = "00:11:22:33:44:55", signalStrength_dBm = -50)
        val device2 = WirelessDevice(name = "Device2", macAddress = "66:77:88:99:AA:BB", signalStrength_dBm = -60)

        val scannedDevicesFlow = MutableStateFlow<List<WirelessDevice>>(listOf(device1, device2))
        every { bleScanController.scannedDevices } returns scannedDevicesFlow

        val result: StateFlow<List<WirelessDevice>> = bluetoothLowEnergyRepository.scannedDevices

        assert(result.value == listOf(device1, device2))
        verify { bleScanController.scannedDevices }
    }
}