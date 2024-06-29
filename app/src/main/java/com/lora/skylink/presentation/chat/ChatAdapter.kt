package com.lora.skylink.presentation.chat


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lora.skylink.databinding.ItemHolderIncomingMessageBinding
import com.lora.skylink.databinding.ItemHolderOutgoingMessageBinding
class ChatAdapter(private var messages: MutableList<ChatMessage> = mutableListOf()) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_USER_MESSAGE_OUTGOING = 10
    private val VIEW_TYPE_USER_MESSAGE_INCOMING = 11
    fun addNewMessage(message: ChatMessage) {
        messages.add(messages.size, message)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            VIEW_TYPE_USER_MESSAGE_OUTGOING  -> {
                val binding = ItemHolderOutgoingMessageBinding.inflate(layoutInflater, parent, false)
                ItemHolderOutgoingMessage(binding)
            }
            VIEW_TYPE_USER_MESSAGE_INCOMING ->  {
                val binding = ItemHolderIncomingMessageBinding.inflate(layoutInflater, parent, false)
                ItemHolderIncomingMessage(binding)
            }
            else -> {
                val binding = ItemHolderOutgoingMessageBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                ) // Generic return
                ItemHolderOutgoingMessage(binding)
            }
        }
    }
    override fun getItemViewType(position: Int): Int {
        return when (val message = messages[position]) {
            //Handle other types of messages FILE/ADMIN ETC
            else -> {
                if (message.type == MessageType.SENT) VIEW_TYPE_USER_MESSAGE_OUTGOING
                else VIEW_TYPE_USER_MESSAGE_INCOMING
            }
        }
    }
    override fun getItemCount() = messages.size
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_USER_MESSAGE_OUTGOING -> {
                holder as ItemHolderOutgoingMessage
                holder.bindView(messages.get(position))
            }
            VIEW_TYPE_USER_MESSAGE_INCOMING -> {
                holder as ItemHolderIncomingMessage
                holder.bindView(messages[position])
            }
            //Handle other types of messages FILE/ADMIN ETC
        }
    }
}