package com.lora.skylink.ui.scan

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lora.skylink.R

class ScanFragment : Fragment() {

    companion object {
        fun newInstance() = ScanFragment()
    }

    private lateinit var viewModel: ScanViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_scan, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ScanViewModel::class.java)
        // TODO: Use the ViewModel
    }

}