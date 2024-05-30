package com.lora.skylink.presentation.chat

enum class MessageType {
    SENT, RECEIVED
}
class ChatMessage {
    var username = "Username"
    var text = "This is a sample ChatMessage"
    var timestamp = "23:39"
    var type = MessageType.SENT
}
