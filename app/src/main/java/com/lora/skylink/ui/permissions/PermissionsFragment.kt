package com.lora.skylink.ui.permissions

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lora.skylink.R
import com.lora.skylink.common.loge
import com.lora.skylink.databinding.FragmentPermissionsBinding
import com.lora.skylink.ui.common.bluetoothAndLocationPermissions
import com.lora.skylink.ui.scan.ScanFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PermissionsFragment : Fragment(R.layout.fragment_permissions) {

    private val viewModel : PermissionsViewModel by viewModels()
    private lateinit var permissionsState : PermissionsState

    @Inject lateinit var bluetoothAdapter: BluetoothAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        permissionsState = buildPermissionsState()

        val binding = FragmentPermissionsBinding.bind(view)
        binding.btnGotoScan.setOnClickListener{
            val action = PermissionsFragmentDirections.actionPermissionsToScanDest()
            findNavController().navigate(action)
        }
    }


    private val BLUETOOTH_PERMISSION_REQUEST_CODE = 200
    private fun requestAllPermissions() {
        requestPermissions( permissionsBluetoothAndLocation, BLUETOOTH_PERMISSION_REQUEST_CODE)
    }
    private fun checkAllPermissions(): Boolean {
        for (permission in permissionsBluetoothAndLocation) {
            if (context?.let { ContextCompat.checkSelfPermission(it, permission) } != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }
    val permissionsBluetoothAndLocation = arrayOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == BLUETOOTH_PERMISSION_REQUEST_CODE) {
            // Check if all permissions were granted
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // All permissions granted
            } else {
                // Permission denied, handle accordingly
            }
        }
    }


    override fun onResume() {
        super.onResume()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkAllPermissions()) {
            //requestLocationPermission()
            requestAllPermissions()
        } else {
            if (!bluetoothAdapter.isEnabled) {
                promptEnableBluetooth()
            }
        }
    }
    private val requestEnableBluetoothLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                loge("BLUETOOTH RADIO IS NOW ON")
            }
        }
    private fun promptEnableBluetooth() {
        bluetoothAdapter?.let { adapter ->
            if (!adapter.isEnabled) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                requestEnableBluetoothLauncher.launch(enableBtIntent)
            }
        }
    }
}