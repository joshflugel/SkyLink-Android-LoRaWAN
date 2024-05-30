package com.lora.skylink.presentation.scan

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.lora.skylink.R
import com.lora.skylink.bluetoothlegacy.ConnectionEventListener
import com.lora.skylink.bluetoothlegacy.ConnectionManager
import com.lora.skylink.bluetoothlegacy.ConnectionManager.isConnected
import com.lora.skylink.common.loge
import com.lora.skylink.databinding.FragmentScanBinding
import com.lora.skylink.presentation.common.PermissionsRequester
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
@AndroidEntryPoint
class ScanFragment : Fragment(R.layout.fragment_scan) {

    private val viewModel: ScanViewModel by viewModels() //     ScanViewModelFactory(requireNotNull(safeArgs.ble)) }
    lateinit var permissionsRequester: PermissionsRequester

    private lateinit var binding: FragmentScanBinding
    private val scanResultAdapter: ScanResultAdapter by lazy {
        ScanResultAdapter(mutableListOf()) { result ->
            if (viewModel.uiState.value.isScanning) {
                viewModel.stopScanning()
            }
            connectToBluetoothLowEnergyDevice(result.device)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        permissionsRequester = PermissionsRequester(this)
        binding = FragmentScanBinding.bind(view)


        binding.scanFragmentToolbar.setNavigationOnClickListener {
            if (!permissionsRequester.checkAllPermissions() ) {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
        val onBackCallback = object : OnBackPressedCallback(true ) {
            override fun handleOnBackPressed() {
                viewModel.checkBluetoothEnabled()  // asks for BTooth if not enabled
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackCallback)
        }

        binding.btnScanDevices.setOnClickListener {
            viewModel.checkBluetoothEnabled()
            if (viewModel.uiState.value.isScanning) {
                viewModel.stopScanning()
            } else {
                viewModel.startScanning()
            }
        }

        setupRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                binding.btnScanDevices.text = if (uiState.isScanning) getString(R.string.stop_scanning)
                else getString(R.string.start_scanning)
                scanResultAdapter.submitList(uiState.scannedDevices)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        ConnectionManager.registerListener(connectionEventListener)

        viewModel.checkBluetoothEnabled()

        if(!permissionsRequester.checkAllPermissions()) {
            navigateToPermissionsFragment()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ConnectionManager.unregisterListener(connectionEventListener)
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

    fun navigateToPermissionsFragment() {
        Toast.makeText(context, "Please activate Bluetooth and Grant Permissions", Toast.LENGTH_LONG).show()
        val action = ScanFragmentDirections.actionScanDestToPermissionsDest()
        findNavController().navigate(action)
    }
    fun navigateToChatFragment(bluetoothDevice: BluetoothDevice) {
        GlobalScope.launch(Dispatchers.Main) {
            val action = ScanFragmentDirections.actionScanDestToChatDest(bluetoothDevice)
            findNavController().navigate(action)
        }
    }

    // MIGRATE TO BLE CONTROLLER / DATA LAYER
    private fun connectToBluetoothLowEnergyDevice(device: BluetoothDevice) {
            loge("Connecting to $device.address")
            println("FLUGEL - isConnectable: ${device.isConnected()}")
            if(device.isConnected()) {
                loge("Navigating to Chat UI... ${device.name}")
                navigateToChatFragment(device)
            } else {
                ConnectionManager.connect(device, context)
            }
    }

    private val connectionEventListener by lazy {
        ConnectionEventListener().apply {
            onConnectionSetupComplete = { gatt ->
                loge("Navigating to Chat UI... ${gatt.device.name}")
                navigateToChatFragment(gatt.device)

                // ConnectionManager.unregisterListener(this)
            }
            onDisconnect = {
                loge("...DISCONNECTED from Bluetooth LoRa Arduino")
            }

        }
    }
}





/*
private val isBluetoothEnabled: Boolean
//get() = bluetoothAdapter?.isEnabled == true
get() = bluetoothArduinoLoraAdapter?.isEnabled == true

*/

/* refactored to DI
    private val bluetoothManager by lazy {
        context?.getSystemService(BluetoothManager::class.java)
    }
    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }
 */

/*
fun arePermissionsGranted(): Boolean {
    return PermissionsRequester(this).checkAllPermissions()
}
 */

/* OBSOLETE
private val bleScanner by lazy {
    bluetoothArduinoLoraAdapter.bluetoothLeScanner
}
//  var bluetoothManager = BluetoothTransceiverManager()



// onCreateView:
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
*/

/*
    private fun checkBluetoothPermissions() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                )
            )
        }
    }



        override fun onResume() {
        super.onResume()

            //checkAllPermissions(this)
        ConnectionManager.registerListener(connectionEventListener)
        //if(!(arePermissionsGranted() && isBluetoothAdapterON())) {
        /*
        if(!viewModel.isBluetoothAdapterENABLED()) {
            loge("ScanFragment - SOME PERMISSIONS WERE MISSING, Returning to PermissionsFragment")
            navigateToPermissionsFragment()
        }


                binding.btnScanDevices.setOnClickListener {
            /*
            //if(!(arePermissionsGranted() && isBluetoothAdapterON())) {
            if(!viewModel.isBluetoothAdapterENABLED()) {
                loge("ScanFragment - SOME PERMISSIONS WERE MISSING, Returning to PermissionsFragment")
                navigateToPermissionsFragment()
            } else {
                //if (isScanning) stopBleScanLEGACY() else startBleScanLEGACY()
                if (isScanning) viewModel.stopScanning() else viewModel.startScanning()
            }
/*
    private val enableBluetoothLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { /* Not needed */ }

    val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val hasBluetoothConnectPermission = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            perms[Manifest.permission.BLUETOOTH_CONNECT] == true
        } else true

        // Launch Bluetootn ON prompt if persision ok but BLE is off
        /*
        if(hasBluetoothConnectPermission && !viewModel.isBluetoothAdapterENABLED()) {
            enableBluetoothLauncher.launch(
                Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            )
        }
        */
        if(hasBluetoothConnectPermission) {
            viewModel.checkBluetoothEnabled()
        }
    }
 */