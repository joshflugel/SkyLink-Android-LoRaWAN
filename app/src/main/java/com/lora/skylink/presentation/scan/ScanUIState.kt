package com.lora.skylink.presentation.scan

import android.bluetooth.le.ScanResult
import androidx.fragment.app.Fragment

fun Fragment.buildScanUIState(
   // navController: NavController = findNavController(),
   // locationBluetoothPermissionRequester: PermissionsRequester = PermissionsRequester(this)
    ) = ScanUIState()
data class ScanUIState (
    val scannedDevices: List<ScanResult> = emptyList(),
    val isScanning: Boolean = false
)
