package com.lora.skylink.presentation.scan

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
import com.lora.skylink.common.loge
import com.lora.skylink.databinding.FragmentScanBinding
import com.lora.skylink.presentation.common.PermissionsRequester
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
            viewModel.connectToDevice(result.device)
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
                viewModel.showEnableBluetoothPromptIfDisabled()
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackCallback)
        }

        binding.btnScanDevices.setOnClickListener {
            viewModel.showEnableBluetoothPromptIfDisabled()
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
        viewModel.showEnableBluetoothPromptIfDisabled()
        if(!permissionsRequester.checkAllPermissions()) {
            navigateToPermissionsFragment()
        }
        viewModel.registerConnectionEventListener(connectionEventListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.unregisterConnectionEventListener(connectionEventListener)
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


    private val connectionEventListener by lazy {
        ConnectionEventListener().apply {
            onConnectionSetupComplete = { gatt ->
                loge("Navigating to Chat UI... ${gatt.device.name}")
                navigateToChatFragment(gatt.device)
            }
            onDisconnect = {
                loge("...DISCONNECTED from Bluetooth LoRa Arduino")
            }
        }
    }
}