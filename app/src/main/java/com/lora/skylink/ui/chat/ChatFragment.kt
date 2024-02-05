package com.lora.skylink.ui.chat


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.lora.skylink.R
import com.lora.skylink.common.loge
import com.lora.skylink.databinding.FragmentChatBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : Fragment(R.layout.fragment_chat) {

    private val viewModel: ChatViewModel by viewModels() //dev ex says
    private lateinit var  chatState: ChatState
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chatState = buildChatState()
        val binding = FragmentChatBinding.bind(view)
        binding.btnSend.setOnClickListener{
            //TODO
            loge("SEND Pressed")
        }
    }
}