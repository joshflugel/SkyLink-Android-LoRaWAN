package com.lora.skylink.presentation.permissions

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.lora.skylink.presentation.common.PermissionsRequester
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


fun Fragment.buildPermissionsState(
    scope: CoroutineScope = viewLifecycleOwner.lifecycleScope,
    navController: NavController = findNavController(),
    locationBluetoothPermissionRequester: PermissionsRequester = PermissionsRequester(this)
    ) = PermissionsState(scope, navController, locationBluetoothPermissionRequester)


class PermissionsState(
    private val scope: CoroutineScope,
    private val navController: NavController,
    private val locationBluetoothPermissionRequester: PermissionsRequester
    ) {
    fun requestPermissions(afterRequest: (Boolean) -> Unit) {
        scope.launch {
            val result = locationBluetoothPermissionRequester.request()
            afterRequest(result)
        }
    }
    fun areAllPermissionsGranted(): Boolean {
        return locationBluetoothPermissionRequester.checkAllPermissions()
    }

    fun navigateToScanFragment() {
        val action = PermissionsFragmentDirections.actionPermissionsToScanDest()
        navController.navigate(action)
    }
}