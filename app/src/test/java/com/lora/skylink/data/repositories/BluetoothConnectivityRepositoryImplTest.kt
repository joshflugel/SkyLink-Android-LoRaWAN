package com.lora.skylink.data.repositories

import android.bluetooth.BluetoothDevice
import android.content.Context
import com.lora.skylink.App
import com.lora.skylink.data.model.WirelessDevice
import com.lora.skylink.data.remote.bluetoothlowenergy.BleConnectionManager
import com.lora.skylink.data.remote.bluetoothlowenergy.ConnectionEventListener
import com.lora.skylink.domain.BluetoothDeviceConverter
import com.lora.skylink.util.Logger
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BluetoothConnectivityRepositoryImplTest {
    // Verbose comments are for learning purposes, as this
    // project is a deliverable for an Android Architecture course

    // Class under test
    private lateinit var repository: BluetoothConnectivityRepositoryImpl
    // Mocks
    private lateinit var deviceConverter: BluetoothDeviceConverter
    private lateinit var context: Context
    private lateinit var logger: Logger
    private lateinit var bleConnectionManager: BleConnectionManager

    @Before
    fun setup() {
        deviceConverter = mockk()
        context = mockk(relaxed = true)
        logger = mockk(relaxed = true)
        bleConnectionManager = mockk(relaxed = true)

        mockkObject(App.Companion)
        every { App.applicationContext() } returns context

        repository = BluetoothConnectivityRepositoryImpl(deviceConverter, bleConnectionManager)
    }

    @After
    fun teardown() {
        clearAllMocks()
    }

    @Test
    fun `connectToDevice should call ConnectionManager connect`() {
        // Mock dependencies
        val wirelessDevice = WirelessDevice("Test Device", "00:11:22:33:44:55", -50)
        val bluetoothDevice = mockk<BluetoothDevice>()

        // Stubbing behavior
        every { deviceConverter.toBluetoothDevice(wirelessDevice) } returns bluetoothDevice

        // Call the method under test
        repository.connectToDevice(wirelessDevice)

        // Verify interactions
        verify { deviceConverter.toBluetoothDevice(wirelessDevice) }
        verify { bleConnectionManager.connect(bluetoothDevice, context) }
    }

    @Test
    fun `disconnectFromDevice should call ConnectionManager disconnectFromDevice`() {
        val wirelessDevice = WirelessDevice("Test Device", "00:11:22:33:44:55", -50)
        val bluetoothDevice = mockk<BluetoothDevice>()

        every { deviceConverter.toBluetoothDevice(wirelessDevice) } returns bluetoothDevice

        repository.disconnectFromDevice(wirelessDevice)

        verify { deviceConverter.toBluetoothDevice(wirelessDevice) }
        verify { bleConnectionManager.disconnectFromDevice(bluetoothDevice) }
    }

    @Test
    fun `teardownConnection should call ConnectionManager disconnectFromDeviceAndReleaseResources`() {
        val wirelessDevice = WirelessDevice("Test Device", "00:11:22:33:44:55", -50)
        val bluetoothDevice = mockk<BluetoothDevice>()

        every { deviceConverter.toBluetoothDevice(wirelessDevice) } returns bluetoothDevice

        repository.teardownConnection(wirelessDevice)

        verify { deviceConverter.toBluetoothDevice(wirelessDevice) }
        verify { bleConnectionManager.disconnectFromDeviceAndReleaseResources(bluetoothDevice) }
    }

    @Test
    fun `registerConnectionEventListener should call ConnectionManager registerListener`() {
        val listener = mockk<ConnectionEventListener>(relaxed = true)

        repository.registerConnectionEventListener(listener)

        verify { bleConnectionManager.registerListener(any()) }
    }

    @Test
    fun `unregisterConnectionEventListener should call ConnectionManager unregisterListener`() {
        val listener = mockk<ConnectionEventListener>(relaxed = true)

        repository.unregisterConnectionEventListener(listener)

        verify { bleConnectionManager.unregisterListener(any()) }
    }
}
