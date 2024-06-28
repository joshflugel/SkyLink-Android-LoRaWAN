package com.lora.skylink.presentation.chat

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.lora.skylink.databinding.ItemHolderOutgoingMessageBinding

class ItemHolderOutgoingMessage(viewBinding: ItemHolderOutgoingMessageBinding) : RecyclerView.ViewHolder(viewBinding.root) {
    private val messageText = viewBinding.textMessageOutgoing
    private val timestamp = viewBinding.textTimestampOutgoing
    private val user = viewBinding.textUserOutgoing

    @SuppressLint("SetTextI18n")
    fun bindView(message: ChatMessage) {
        messageText.text = message.text
        timestamp.text = message.timestamp
        user.text = message.username + ":"
    }
}