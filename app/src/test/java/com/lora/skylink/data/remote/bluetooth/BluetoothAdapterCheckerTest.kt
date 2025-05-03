package com.lora.skylink.data.remote.bluetooth

import android.bluetooth.BluetoothAdapter
import com.lora.skylink.data.framework.bluetooth.scanning.BluetoothReadyCheckerImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

//class BluetoothAdapterManagerTest {
class BluetoothAdapterCheckerTest {

    private lateinit var bluetoothAdapterChecker: BluetoothReadyCheckerImpl
    private lateinit var bluetoothAdapter: BluetoothAdapter

    @Before
    fun setup() {
        bluetoothAdapter = mockk()
        bluetoothAdapterChecker = BluetoothReadyCheckerImpl(bluetoothAdapter)
    }

    @Test
    fun `isBluetoothAdapterEnabled should return true when Bluetooth is enabled`() {
        every { bluetoothAdapter.isEnabled } returns true

        val result = bluetoothAdapterChecker.isBluetoothAdapterReady()

        assert(result)
        verify { bluetoothAdapter.isEnabled }
    }

    @Test
    fun `isBluetoothAdapterEnabled should return false when Bluetooth is disabled`() {
        every { bluetoothAdapter.isEnabled } returns false

        val result = bluetoothAdapterChecker.isBluetoothAdapterReady()

        assert(!result)
        verify { bluetoothAdapter.isEnabled }
    }
}