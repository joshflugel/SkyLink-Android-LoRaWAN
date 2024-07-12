package com.lora.skylink.presentation.permissions

import android.app.Activity.RESULT_OK
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.lora.skylink.R
import com.lora.skylink.databinding.FragmentPermissionsBinding
import com.lora.skylink.util.loge
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PermissionsFragment : Fragment(R.layout.fragment_permissions) {

    private val viewModel : PermissionsViewModel by viewModels()
    @Inject lateinit var permissionsStateFactory: PermissionsStateFactory
    private lateinit var permissionsState : PermissionsState
    @Inject lateinit var bluetoothAdapter: BluetoothAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        println("FLUGEL - PermissionsFrag.onResume")
        
        permissionsState = permissionsStateFactory.create()

        val binding = FragmentPermissionsBinding.bind(view)
        binding.btnGrantPermissions.setOnClickListener{
            handleScanDevicesButtonClicked()
        }

    }

    override fun onResume() {
        super.onResume()
        println("FLUGEL - PermissionsFrag.onResume")
        if (permissionsState.areAllPermissionsGranted() && isBluetoothAdapterReady()) {
            loge("ALL PERMISSIONS OK, BLUETOOTH DEVICE OK... proceeding to SCAN UI")
            permissionsState.navigateToScanFragment()
        }
    }

    private fun handleScanDevicesButtonClicked() {
            if (permissionsState.areAllPermissionsGranted() && isBluetoothAdapterReady()) {
                loge("ALL PERMISSIONS OK")
                permissionsState.navigateToScanFragment()
            } else {
                loge("PERMISSIONS MISSING")
                viewLifecycleOwner.lifecycleScope.launch {
                    permissionsState.requestPermissions { allGranted ->
                        if (allGranted) {
                            loge("ALL PERMISSIONS NOW **GRANTED**, navigating to ScanFragment")
                            if(isBluetoothAdapterReady()) {
                                permissionsState.navigateToScanFragment()
                            } else {
                                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                                requestEnableBluetoothLauncher.launch(enableBtIntent)
                            }
                        } else {
                            loge("SOME PERMISSIONS ARE STILL MISSING, goto Android-> Settings")
                            handleMissingPermissions()
                        }
                    }
                }
            }
    }

    private fun isBluetoothAdapterReady():Boolean {
        bluetoothAdapter.let { adapter ->
            if (adapter.isEnabled) {
                return true
            }
        }
        return false
    }

    private val requestEnableBluetoothLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                loge("BLUETOOTH RADIO IS NOW ON")
                permissionsState.navigateToScanFragment()
            }
        }

    private fun handleMissingPermissions() {
        viewModel.incrementPermissionRequestRetries()
        if (viewModel.userHasDeniedPermissions()) {
            loge("PERMISSIONS DENIED, LAUNCHING MANUAL SETTINGS")
            launchAppPermissionSettings()
        }
    }
    private fun launchAppPermissionSettings() {
        val intentLaunchAppManualPermissionSettings = Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", requireActivity().packageName, null)
        }
        startActivity(intentLaunchAppManualPermissionSettings)
    }

}