package com.lora.skylink.presentation.permissions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.lora.skylink.presentation.common.PermissionsRequester
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
@Config(manifest = Config.NONE)
class PermissionsStateIntegrationTest {

    private lateinit var permissionsState: PermissionsState
    private val navController: NavController = mock(NavController::class.java)
    private val permissionsRequester: PermissionsRequester = mock(PermissionsRequester::class.java)
    private lateinit var lifecycleOwner: LifecycleOwner
    private lateinit var lifecycle: LifecycleRegistry

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        lifecycleOwner = mock(LifecycleOwner::class.java)
        lifecycle = LifecycleRegistry(lifecycleOwner)
        `when`(lifecycleOwner.lifecycle).thenReturn(lifecycle)
        permissionsState = PermissionsState(lifecycleOwner.lifecycleScope, navController, permissionsRequester)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test all permissions granted`() {
        `when`(permissionsRequester.checkAllPermissions()).thenReturn(true)
        assert(permissionsState.areAllPermissionsGranted())
    }

    @Test
    fun `test requesting permissions`() = runTest {
        `when`(permissionsRequester.request()).thenReturn(true)

        permissionsState.requestPermissions { allGranted ->
            assert(allGranted)
        }
    }

    @Test
    fun `test navigation to scan fragment`() = runTest {
        val directions = PermissionsFragmentDirections.actionPermissionsToScanDest()

        doNothing().`when`(navController).navigate(eq(directions))

        permissionsState.navigateToScanFragment()

        verify(navController).navigate(any<NavDirections>())
    }
}
