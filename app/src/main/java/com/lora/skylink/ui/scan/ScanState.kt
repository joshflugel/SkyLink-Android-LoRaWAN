package com.lora.skylink.ui.scan

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.lora.skylink.ui.permissions.PermissionsFragmentDirections

fun Fragment.buildScanState(
    navController: NavController = findNavController()
    ) = ScanState(navController)
class ScanState (private val navController: NavController){
    fun onGoToChatClicked() {
        val action = ScanFragmentDirections.actionScanDestToChatDest()
        navController.navigate(action)
    }
}