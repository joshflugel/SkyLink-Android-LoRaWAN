package com.lora.skylink.data.remote.bluetooth

import android.bluetooth.BluetoothAdapter
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class BluetoothAdapterManagerTest {

    private lateinit var bluetoothAdapterManager: BluetoothAdapterManager
    private lateinit var bluetoothAdapter: BluetoothAdapter

    @Before
    fun setup() {
        bluetoothAdapter = mockk()
        bluetoothAdapterManager = BluetoothAdapterManager(bluetoothAdapter)
    }

    @Test
    fun `isBluetoothAdapterEnabled should return true when Bluetooth is enabled`() {
        every { bluetoothAdapter.isEnabled } returns true

        val result = bluetoothAdapterManager.isBluetoothAdapterEnabled()

        assert(result)
        verify { bluetoothAdapter.isEnabled }
    }

    @Test
    fun `isBluetoothAdapterEnabled should return false when Bluetooth is disabled`() {
        every { bluetoothAdapter.isEnabled } returns false

        val result = bluetoothAdapterManager.isBluetoothAdapterEnabled()

        assert(!result)
        verify { bluetoothAdapter.isEnabled }
    }
}