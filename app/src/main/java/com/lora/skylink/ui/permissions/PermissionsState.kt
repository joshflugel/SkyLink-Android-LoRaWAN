package com.lora.skylink.ui.permissions

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.lora.skylink.ui.common.PermissionsRequester
import com.lora.skylink.ui.common.bluetoothAndLocationPermissions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


fun Fragment.buildPermissionsState(
    // context: Context = requireContext(),
    scope: CoroutineScope = viewLifecycleOwner.lifecycleScope,
    navController: NavController = findNavController(),
    locationPermissionRequester: PermissionsRequester = PermissionsRequester(
            this,
            bluetoothAndLocationPermissions
        )
    ) = PermissionsState(scope, navController, locationPermissionRequester)

class PermissionsState(
    //private val context: Context,
    private val scope: CoroutineScope,
    private val navController: NavController,
    private val permissionsRequester: PermissionsRequester
    ) {
        fun onScanDevicesClicked() {
            val action = PermissionsFragmentDirections.actionPermissionsToScanDest()
            navController.navigate(action)
        }

        fun requestPermissions(afterRequest: (Boolean) -> Unit) {
            scope.launch {
                val result = permissionsRequester.request()
                afterRequest(result)
            }
        }
}