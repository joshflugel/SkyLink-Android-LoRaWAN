package com.lora.skylink.ui.chat

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.lora.skylink.databinding.ItemHolderIncomingMessageBinding

class ItemHolderIncomingMessage(viewBinding: ItemHolderIncomingMessageBinding) : RecyclerView.ViewHolder(viewBinding.root) {
    private val messageText = viewBinding.textMessageIncoming
    private val timestamp = viewBinding.textTimestampIncoming
    private val user = viewBinding.textUserIncoming

    @SuppressLint("SetTextI18n")
    fun bindView(message: ChatMessage) {
        messageText.text = message.text
        timestamp.text = message.timestamp
        user.text = message.username + ":"
    }
}