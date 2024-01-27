package com.lora.skylink.ui.permissions

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

/* TO-DO
fun Fragment.buildPermissionsState(
    //context: Context = requireContext(),
    //scope: CoroutineScope = viewLifecycleOwner.lifecycleScope,
    navController: NavController = findNavController(),
    //locationPermissionRequester: PermissionRequester = PermissionRequester(
    //    this,
    //    Manifest.permission.ACCESS_COARSE_LOCATION
    //)
) = //PermissionsState(context, scope, navController, locationPermissionRequester)
    PermissionsState(navController)
 */


fun Fragment.buildPermissionsState(
        navController: NavController = findNavController()
    ) = PermissionsState(navController)

class PermissionsState(private val navController: NavController) {
        fun onScanDevicesClicked() {
            val action = PermissionsFragmentDirections.actionPermissionsToScanDest()
            navController.navigate(action)
        }
}