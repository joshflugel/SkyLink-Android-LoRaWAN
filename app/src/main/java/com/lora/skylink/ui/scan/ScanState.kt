package com.lora.skylink.ui.scan

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.lora.skylink.ui.common.PermissionsRequester

fun Fragment.buildScanState(
    navController: NavController = findNavController(),
    locationBluetoothPermissionRequester: PermissionsRequester = PermissionsRequester(this)
    ) = ScanState(navController, locationBluetoothPermissionRequester)
class ScanState (
    private val navController: NavController,
    private val locationBluetoothPermissionRequester: PermissionsRequester
) {
    fun arePermissionsGranted(): Boolean {
        return locationBluetoothPermissionRequester.checkAllPermissions()
    }
    fun navigateToPermissionsFragment() {
        val action = ScanFragmentDirections.actionScanDestToPermissionsDest()
        navController.navigate(action)
    }
    fun onGoToChatClicked() {
        val action = ScanFragmentDirections.actionScanDestToChatDest()
        navController.navigate(action)
    }
}