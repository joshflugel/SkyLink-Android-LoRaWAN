package com.lora.skylink.ui.scan

import android.bluetooth.BluetoothDevice
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.lora.skylink.ui.common.PermissionsRequester
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
        Toast.makeText(navController.context, "Please activate Bluetooth and Grant Permissions", Toast.LENGTH_LONG).show()
        val action = ScanFragmentDirections.actionScanDestToPermissionsDest()
        navController.navigate(action)
    }
    fun navigateToChatFragment(bluetoothDevice: BluetoothDevice) {
        GlobalScope.launch(Dispatchers.Main) {
            val action = ScanFragmentDirections.actionScanDestToChatDest(bluetoothDevice)
            navController.navigate(action)
        }
    }

}