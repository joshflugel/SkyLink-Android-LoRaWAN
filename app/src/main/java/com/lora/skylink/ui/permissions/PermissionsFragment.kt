package com.lora.skylink.ui.permissions

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lora.skylink.R

class PermissionsFragment : Fragment() {

    companion object {
        fun newInstance() = PermissionsFragment()
    }

    private lateinit var viewModel: PermissionsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_permissions, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PermissionsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}