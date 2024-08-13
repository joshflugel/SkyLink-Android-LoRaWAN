package com.lora.skylink.presentation.scan

import app.cash.turbine.test
import com.lora.skylink.domain.CheckBluetoothEnabledUseCase
import com.lora.skylink.domain.ManageBluetoothDeviceConnectionUseCase
import com.lora.skylink.domain.ScanBluetoothLowEnergyDevicesUseCase
import com.lora.skylink.presentation.common.MainDispatcherRule
import com.lora.skylink.presentation.common.mockDeviceStateFlow
import com.lora.skylink.presentation.common.mockDevices
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class ScanStateIntegrationTest {

    @get:Rule
    val rule = MainDispatcherRule()

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)
    private val testScope = TestScope(testDispatcher)

    private lateinit var viewModel: ScanViewModel
    private lateinit var scanBluetoothLeDevicesUseCase: ScanBluetoothLowEnergyDevicesUseCase
    private lateinit var checkBluetoothEnabledUseCase: CheckBluetoothEnabledUseCase
    private lateinit var manageBluetoothDeviceConnectionUseCase: ManageBluetoothDeviceConnectionUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        scanBluetoothLeDevicesUseCase = mock(ScanBluetoothLowEnergyDevicesUseCase::class.java)
        checkBluetoothEnabledUseCase = mock(CheckBluetoothEnabledUseCase::class.java)
        manageBluetoothDeviceConnectionUseCase = mock(ManageBluetoothDeviceConnectionUseCase::class.java)
        viewModel = ScanViewModel(scanBluetoothLeDevicesUseCase, checkBluetoothEnabledUseCase, manageBluetoothDeviceConnectionUseCase)
    }

    @Test
    fun `startScanning and stopScanning update UIState and list of scannedDevices`() = testScope.runTest {

        whenever(scanBluetoothLeDevicesUseCase()).thenReturn(mockDeviceStateFlow)
        doNothing().`when`(scanBluetoothLeDevicesUseCase).startScanning()

        viewModel.uiState.test {

            assertEquals(ScanUIState(isScanning = false, scannedDevices = emptyList(), isWirelessDeviceEnabled = true), awaitItem())

            viewModel.startScanning()

            assertEquals(ScanUIState(isScanning = true, scannedDevices = emptyList()), awaitItem())
            assertEquals(ScanUIState(isScanning = true, scannedDevices = mockDevices), awaitItem())
            println("ScanViewModel Test mockDevices: $mockDevices")

            viewModel.stopScanning()

            assertEquals(ScanUIState(isScanning = false, scannedDevices = emptyList()), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

}