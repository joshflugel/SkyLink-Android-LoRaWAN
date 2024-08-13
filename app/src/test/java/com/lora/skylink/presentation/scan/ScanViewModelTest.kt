package com.lora.skylink.presentation.scan

import com.lora.skylink.domain.CheckBluetoothEnabledUseCase
import com.lora.skylink.domain.ManageBluetoothDeviceConnectionUseCase
import com.lora.skylink.domain.ScanBluetoothLowEnergyDevicesUseCase
import com.lora.skylink.presentation.common.MainDispatcherRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class ScanViewModelTest {

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
    fun `WirelessDeviceEnabled state is True and Wireless device prompt IS NOT shown when the phone's Bluetooth Device is Enabled`() = testScope.runTest {
        whenever(checkBluetoothEnabledUseCase()).thenReturn(true)

        viewModel.showEnableWirelessDevicePromptIfDisabled()

        advanceUntilIdle()
        assertTrue ( viewModel.uiState.value.isWirelessDeviceEnabled )
    }

    @Test
    fun `WirelessDeviceEnabled state is False and Wireless device prompt IS shown when the phone's Bluetooth Device is Disabled`() = testScope.runTest {
        whenever(checkBluetoothEnabledUseCase()).thenReturn(false)

        viewModel.showEnableWirelessDevicePromptIfDisabled()

        advanceUntilIdle()
        assertFalse(viewModel.uiState.value.isWirelessDeviceEnabled)
    }

}