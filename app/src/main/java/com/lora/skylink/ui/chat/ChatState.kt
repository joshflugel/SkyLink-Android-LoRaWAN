package com.lora.skylink.ui.chat

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.lora.skylink.ui.permissions.PermissionsFragmentDirections

fun Fragment.buildChatState(
    navController: NavController = findNavController()
    ) = ChatState(navController)
class ChatState (private val navController: NavController) {
    fun onSendClicked() {
        // TODO TxMessage
    }
}
