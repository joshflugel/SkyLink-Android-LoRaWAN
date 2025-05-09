package com.lora.skylink.data.remote.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanSettings
import com.lora.skylink.data.framework.bluetooth.scanning.BluetoothLowEnergyScanController
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class) // Allows for unit testing Android framework classes on the JVM.
@Config(sdk = [28], manifest = Config.NONE)
class BluetoothLowEnergyScanControllerTest {

    private lateinit var bleScanController: BluetoothLowEnergyScanController
    private lateinit var bluetootAdapter: BluetoothAdapter
    private lateinit var bleScanner: BluetoothLeScanner

    @Before
    fun setup () {
        bluetootAdapter = mockk(relaxed = true)
        bleScanner = mockk(relaxed = true)

        every { bluetootAdapter.bluetoothLeScanner } returns bleScanner

        bleScanController = BluetoothLowEnergyScanController(bluetootAdapter)
    }

    @Test
    fun `startBleScan() should start BLE scanning`() {
        bleScanController.startBleScan()

        verify { bleScanner.startScan(null, any<ScanSettings>(), any<ScanCallback>()) }
    }


    @Test
    fun `stopBleScan() should stop BLE scanning`() {
        bleScanController.stopBleScan()

        verify { bleScanner.stopScan(any<ScanCallback>()) }
    }
}