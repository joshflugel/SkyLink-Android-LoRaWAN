package com.lora.skylink.presentation.permissions

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class PermissionsViewModelTest {

    private lateinit var viewModel: PermissionsViewModel

    @Before
    fun setup() {
        viewModel = PermissionsViewModel()
    }

    @Test
    fun `test incrementin of permission request retries`() = runTest {
        viewModel.incrementPermissionRequestRetries()
        viewModel.incrementPermissionRequestRetries()
        assertEquals(2, viewModel.retries.value)
    }

    @Test
    fun `test user has denied permissions`() = runTest {
        viewModel.incrementPermissionRequestRetries()
        viewModel.incrementPermissionRequestRetries()
        viewModel.incrementPermissionRequestRetries()
        assert(viewModel.userHasDeniedPermissions())
    }
}
