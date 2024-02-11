package com.lora.skylink.ui.chat

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun Fragment.buildChatState(
    navController: NavController = findNavController()
    ) = ChatState(navController)
class ChatState (private val navController: NavController) {
    fun onSendClicked() {
        // TODO TxMessage
    }

    fun navigateToScanFragment() {
        GlobalScope.launch(Dispatchers.Main) {
            val action = ChatFragmentDirections.actionChatDestToScanDest()
            navController.navigate(action)
        }
    }
}
