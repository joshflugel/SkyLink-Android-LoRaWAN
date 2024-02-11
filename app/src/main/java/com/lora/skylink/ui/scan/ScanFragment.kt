package com.lora.skylink.ui.scan

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.lora.skylink.R
import com.lora.skylink.bluetooth.BluetoothTransceiverManager
import com.lora.skylink.bluetooth.ConnectionEventListener
import com.lora.skylink.bluetooth.ConnectionManager
import com.lora.skylink.bluetooth.ConnectionManager.isConnected
import com.lora.skylink.common.loge
import com.lora.skylink.common.logi
import com.lora.skylink.databinding.FragmentScanBinding
import com.lora.skylink.ui.common.PermissionsRequester
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("MissingPermission")
@AndroidEntryPoint
class ScanFragment : Fragment(R.layout.fragment_scan) {




    /*
    private val viewModel: ScanViewModel by viewModels {
        ScanViewModelFactory(requireNotNull(safeArgs.ble))
    }

     */


    //private val viewModel: ScanViewModel by viewModels()
    private lateinit var scanState : ScanState
    private lateinit var binding: FragmentScanBinding
    //private val adapter = ScanResultAdapter { scanState.onTransceiverDeviceClicked(it)}


    var permissionsRequester = PermissionsRequester(this)


    @Inject lateinit var bluetoothArduinoLoraAdapter: BluetoothAdapter
    private val bleScanner by lazy {
        bluetoothArduinoLoraAdapter.bluetoothLeScanner
    }
    var bluetoothManager = BluetoothTransceiverManager()





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        scanState = buildScanState()

        binding = FragmentScanBinding.bind(view)
        binding.scanFragmentToolbar.setNavigationOnClickListener {
            if (!permissionsRequester.checkAllPermissions()) {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
        val onBackCallback = object : OnBackPressedCallback(true ) {
            override fun handleOnBackPressed() {
                if(!scanState.arePermissionsGranted() || !isBluetoothAdapterON()) {
                    scanState.navigateToPermissionsFragment()
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackCallback)
        }
        /*
        binding.btnGoToChat.setOnClickListener{
            if(scanState.arePermissionsGranted() && isBluetoothAdapterReady()){
                loge("ScanFragment - ALL PERMISSIONS GREEN")
                scanState.navigateToChatFragment()
            }
            else{
                loge("ScanFragment - SOME PERMISSIONS WERE MISSING, Returning to PermissionsFragment")
                scanState.navigateToPermissionsFragment()
            }
        }
         */

        binding.btnScanDevices.setOnClickListener {
            if(!scanState.arePermissionsGranted() ||  !isBluetoothAdapterON()){
                loge("ScanFragment - SOME PERMISSIONS WERE MISSING, Returning to PermissionsFragment")
                scanState.navigateToPermissionsFragment()
            } else {
                if (isScanning) stopBleScan() else startBleScan()
            }

        }
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        ConnectionManager.registerListener(connectionEventListener)
        if(!(scanState.arePermissionsGranted() && isBluetoothAdapterON())) {
            loge("ScanFragment - SOME PERMISSIONS WERE MISSING, Returning to PermissionsFragment")
            scanState.navigateToPermissionsFragment()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ConnectionManager.unregisterListener(connectionEventListener)
    }


    fun isBluetoothAdapterON():Boolean {
        bluetoothArduinoLoraAdapter.let { adapter ->
            if (adapter.isEnabled) {
                return true
            }
        }
        return false
    }


    private var isScanning = false
        set(value) {
            field = value
           // runOnUiThread {
            lifecycleScope.launch(Dispatchers.Main) {
                binding.btnScanDevices.text = if (value) "Stop Scanning" else "Start Scanning"
            }
        }
    private val scanResults = mutableListOf<ScanResult>()
    private val scanResultAdapter: ScanResultAdapter by lazy {
        ScanResultAdapter(scanResults) { result ->
            if (isScanning) {
                stopBleScan()
            }
            with(result.device) {
                loge("Connecting to $address")
                if(result.device.isConnected()) {
                    loge("Navigating to Chat UI... ${result.device.name}")
                    scanState.navigateToChatFragment(result.device)
                } else {
                    ConnectionManager.connect(this, context) //ToDo context has ?
                }
            }
        }
    }



    /*******************************************
     * Callback bodies
     *******************************************/

    private val scanCallback = object : ScanCallback() {
        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val indexQuery = scanResults.indexOfFirst { it.device.address == result.device.address }
            if (indexQuery != -1) { // A scan result already exists with the same address
                scanResults[indexQuery] = result
                scanResultAdapter.notifyItemChanged(indexQuery)
            } else {
                with(result.device) {
                    logi("Found BLE device! Name: ${name ?: "Unnamed"}, address: $address")
                    if(name?.startsWith("LORA")==true) {
                        scanResults.add(result)
                        scanResultAdapter.notifyItemInserted(scanResults.size - 1)
                    }
                }

            }
        }
        override fun onScanFailed(errorCode: Int) {
            loge("onScanFailed: code $errorCode")
        }
    }
    @SuppressLint("MissingPermission")
    private fun startBleScan() {
        if (!permissionsRequester.checkAllPermissions()) {
            scanState.navigateToPermissionsFragment()
        } else {
            scanResults.clear()
            scanResultAdapter.notifyDataSetChanged()
            bleScanner.startScan(null, bluetoothManager.scanSettings, scanCallback)
            isScanning = true
        }
    }
    private fun stopBleScan() {
        // if (!permissionsRequester.checkAllPermissions()) {
        //    scanState.navigateToPermissionsFragment()
        // } else {
        if (isScanning) {
            bleScanner.stopScan(scanCallback)
            isScanning = false
        }
    }

    private fun setupRecyclerView() {
        binding.scanResultsRecyclerView.apply {
            adapter = scanResultAdapter
            layoutManager = LinearLayoutManager(
                this@ScanFragment.requireContext(),
                RecyclerView.VERTICAL,
                false
            )
            isNestedScrollingEnabled = false
        }

        val animator = binding.scanResultsRecyclerView.itemAnimator
        if (animator is SimpleItemAnimator) {
            animator.supportsChangeAnimations = false
        }
    }

    private val connectionEventListener by lazy {
        ConnectionEventListener().apply {
            onConnectionSetupComplete = { gatt ->
                loge("Navigating to Chat UI... ${gatt.device.name}")
                scanState.navigateToChatFragment(gatt.device)


           //     bluetoothArduinoLoraAdapter


                //ConnectionManager.unregisterListener(this)
            }
            onDisconnect = {
                loge("...DISCONNECTED from Bluetooth LoRa Arduino")
            }
        }
    }
}