package com.lora.skylink.ui.scan

import android.bluetooth.BluetoothAdapter
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.lora.skylink.R
import com.lora.skylink.common.loge
import com.lora.skylink.databinding.FragmentScanBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class ScanFragment : Fragment(R.layout.fragment_scan) {

    private val viewModel: ScanViewModel by viewModels()
    private lateinit var scanState : ScanState

    @Inject lateinit var bluetoothAdapter: BluetoothAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        scanState = buildScanState()

        val binding = FragmentScanBinding.bind(view)
        binding.scanFragmentToolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.btnGoToChat.setOnClickListener{
            //scanState.onGoToChatClicked()
            if(scanState.arePermissionsGranted()){
                loge("ScanFragment - ALL PERMISSIONS GREEN")
            }
            else{
                loge("ScanFragment - SOME PERMISSIONS WERE MISSING, Returning to PermissionsFragment")
                scanState.navigateToPermissionsFragment()
            }
        }

        val callback = object : OnBackPressedCallback(true ) {
            override fun handleOnBackPressed() {
                if(!scanState.arePermissionsGranted() || !isBluetoothAdapterReady()) {
                    scanState.navigateToPermissionsFragment()
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        }

    }

    override fun onResume() {
        super.onResume()
        if(!(scanState.arePermissionsGranted() && isBluetoothAdapterReady())) {
            scanState.navigateToPermissionsFragment()
        }
    }

    fun isBluetoothAdapterReady():Boolean {
        bluetoothAdapter.let { adapter ->
            if (adapter.isEnabled) {
                return true
            }
        }
        return false
    }

}