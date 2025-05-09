package com.lora.skylink.presentation.permissions

import app.cash.turbine.test
import com.lora.skylink.domain.BluetoothReadyChecker
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class PermissionsViewModelTest {

    private lateinit var viewModel: PermissionsViewModel
    private lateinit var bluetoothReadyChecker: BluetoothReadyChecker

    @Before
    fun setup() {
        bluetoothReadyChecker = mockk()

        every { bluetoothReadyChecker.isBluetoothAdapterReady() } returns true
        viewModel = PermissionsViewModel(bluetoothReadyChecker)
    }

    @Test
    fun `test incrementing permission request retries`() = runTest {
        viewModel.incrementPermissionRequestRetries()
        viewModel.incrementPermissionRequestRetries()
        assertEquals(2, viewModel.retries.value)
    }

    @Test
    fun `test when user has denied permissions`() = runTest {
        viewModel.incrementPermissionRequestRetries()
        viewModel.incrementPermissionRequestRetries()
        viewModel.incrementPermissionRequestRetries()
        assert(viewModel.userHasDeniedPermissions())
    }

    @Test
    fun `test PermissionRequestRetries Flow`() = runTest {
        viewModel.permissionRequestRetries.test {
            assertEquals(0, awaitItem())
            viewModel.incrementPermissionRequestRetries()
            assertEquals(1, awaitItem())
            viewModel.incrementPermissionRequestRetries()
            assertEquals(2, awaitItem())
        }
    }
}
