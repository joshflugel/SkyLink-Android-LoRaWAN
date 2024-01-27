package com.lora.skylink.ui.permissions

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.lora.skylink.R
import com.lora.skylink.databinding.FragmentPermissionsBinding

class PermissionsFragment : Fragment(R.layout.fragment_permissions) {

    private val viewModel : PermissionsViewModel by viewModels()
    private lateinit var permissionsState : PermissionsState

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        permissionsState = buildPermissionsState()
        val binding = FragmentPermissionsBinding.bind(view)
        binding.btnGotoScan.setOnClickListener{
            permissionsState.onScanDevicesClicked()
        }
    }
}