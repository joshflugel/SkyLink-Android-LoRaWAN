package com.lora.skylink.ui.scan

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.lora.skylink.R
import com.lora.skylink.databinding.FragmentScanBinding
import com.lora.skylink.ui.common.launchAndCollect

class ScanFragment : Fragment(R.layout.fragment_scan) {

    // Android style MVVM
    /*
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
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ScanViewModel::class.java)
        // TODO: Use the ViewModel
    }*/

    private val viewModel: ScanViewModel by viewModels()
    private lateinit var scanState : ScanState

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        scanState = buildScanState()
        val binding = FragmentScanBinding.bind(view)
        binding.scanFragmentToolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.btnGoToChat.setOnClickListener{
            scanState.onGoToChatClicked()
        }

        /* To Do
        viewLifecycleOwner.launchAndCollect(viewModel.state) {

        }
         */
    }
}