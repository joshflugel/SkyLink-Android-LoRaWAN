package com.lora.skylink.presentation.chat

enum class MessageType {
    SENT, RECEIVED
}
class ChatMessage {
    var username = "Username"
    var text = "Some text"
    var timestamp = "0:00"
    var type = MessageType.SENT
}
