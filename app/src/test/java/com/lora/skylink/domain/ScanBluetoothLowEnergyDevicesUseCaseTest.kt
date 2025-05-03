package com.lora.skylink.domain


import com.lora.skylink.data.model.WirelessDevice
import com.lora.skylink.data.repositories.BluetoothLowEnergyRepositoryImpl
import com.lora.skylink.domain.usecases.ScanBluetoothLowEnergyDevicesUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.justRun
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Before
import org.junit.Test

class ScanBluetoothLowEnergyDevicesUseCaseTest {

    @MockK
    private lateinit var repository: BluetoothLowEnergyRepositoryImpl

    private lateinit var useCase: ScanBluetoothLowEnergyDevicesUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        useCase = ScanBluetoothLowEnergyDevicesUseCase(repository)
    }

    @Test
    fun `startScanning should call startBleScan on repository`() {
        justRun { repository.startBleScan() }

        useCase.startScanning()

        verify { repository.startBleScan() }
    }

    @Test
    fun `stopScanning should call stopBleScan on repository`() {
        justRun { repository.stopBleScan() }
        repository.stopBleScan()

        useCase.stopScanning()

        verify { repository.stopBleScan() }
    }

    @Test
    fun `use case invoke should return scannedDevices from repository`() {
        val device1 = WirelessDevice(name = "Device1", macAddress = "00:11:22:33:44:55", signalStrength_dBm = -50)
        val device2 = WirelessDevice(name = "Device2", macAddress = "66:77:88:99:AA:BB", signalStrength_dBm = -60)

        val scannedDevicesFlow = MutableStateFlow(listOf(device1, device2))

        every { repository.scannedDevices } returns scannedDevicesFlow
        val result: StateFlow<List<WirelessDevice>> = useCase()


        assert(result == scannedDevicesFlow)

        verify { repository.scannedDevices }
    }
}