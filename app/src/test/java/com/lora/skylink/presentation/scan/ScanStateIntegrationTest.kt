package com.lora.skylink.presentation.scan

import app.cash.turbine.test
import com.lora.skylink.presentation.common.mockDeviceStateFlow
import com.lora.skylink.presentation.common.mockDevices
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.doNothing
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class ScanStateIntegrationTest: BaseScanViewModelTest() {

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