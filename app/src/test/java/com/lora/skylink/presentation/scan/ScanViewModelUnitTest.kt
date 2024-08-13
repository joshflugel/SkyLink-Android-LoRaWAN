package com.lora.skylink.presentation.scan

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class ScanViewModelUnitTest: BaseScanViewModelTest() {

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