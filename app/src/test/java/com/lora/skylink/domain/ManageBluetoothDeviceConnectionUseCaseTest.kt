package com.lora.skylink.domain

import com.lora.skylink.bluetoothlegacy.ConnectionEventListener
import com.lora.skylink.data.model.WirelessDevice
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test


class ManageBluetoothDeviceConnectionUseCaseTest {

    private lateinit var bluetoothConnectivityRepository: IBluetoothConnectivityRepository
    private lateinit var manageBluetoothDeviceConnectionUseCase: ManageBluetoothDeviceConnectionUseCase
    private val device = WirelessDevice(name = "TestBTDevice", macAddress = "00:11:22:33:44:55", signalStrength_dBm = -50)

    @Before
    fun setup() {
        bluetoothConnectivityRepository = mockk(relaxed = true)
        manageBluetoothDeviceConnectionUseCase = ManageBluetoothDeviceConnectionUseCase(bluetoothConnectivityRepository)
    }

    @Test
    fun `connectToDevice should call repository connect method`() {
        manageBluetoothDeviceConnectionUseCase.connectToDevice(device)
        verify { bluetoothConnectivityRepository.connectToDevice(device) }
    }

    @Test
    fun `disconnectFromDevice should call repository disconnect method`() {
        manageBluetoothDeviceConnectionUseCase.disconnectFromDevice((device))
        verify {bluetoothConnectivityRepository.disconnectFromDevice(device) }
    }

    @Test
    fun `registerConnectionEventListener should call repository register method`() {
        val listener = ConnectionEventListener()
        manageBluetoothDeviceConnectionUseCase.registerConnectionEventListener(listener)
        verify { bluetoothConnectivityRepository.registerConnectionEventListener(listener) }
    }

    @Test
    fun `unregisterConnectionEventListener should call repository unregister method`() {
        val listener = ConnectionEventListener()
        manageBluetoothDeviceConnectionUseCase.unregisterConnectionEventListener(listener)
        verify { bluetoothConnectivityRepository.unregisterConnectionEventListener(listener) }
    }
}