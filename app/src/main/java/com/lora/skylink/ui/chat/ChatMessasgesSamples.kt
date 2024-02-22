package com.lora.skylink.ui.chat




val message1 = ChatMessage().apply {
    text = "Hello there!"
    timestamp = "12:00"
    type = MessageType.SENT
}


val message2 = ChatMessage().apply {
    text = "How are you?"
    timestamp = "12:05"
    type = MessageType.SENT
}


val message3 = ChatMessage().apply {
    text = "I'm doing well, thanks!"
    timestamp = "12:10"
    type = MessageType.RECEIVED
}


val message3a = ChatMessage().apply {
    text = "The goal of the principles is the creation of mid-level software structures that: • Tolerate change, • Are easy to understand, and • Are the basis of components that can be used in many software systems."
    timestamp = "12:10"
    type = MessageType.RECEIVED
}


val message3b = ChatMessage().apply {
    text = "LoRa and LoRaWAN define a low-power, wide-area (LPWA) networking protocol designed to wirelessly connect battery operated devices to the internet in regional, national or global networks, and targets key Internet of things (IoT) requirements such as bi-directional communication, end-to-end security, mobility and localization services."
    timestamp = "12:14"
    type = MessageType.SENT
}

val message4 = ChatMessage().apply {
    text = "What about you?"
    timestamp = "12:15"
    type = MessageType.RECEIVED
}

val message5 = ChatMessage().apply {
    text = "Run with More Threads Than Processors Things happen when the system switches between tasks. To encourage task swapping, run with more threads than processors or cores. The more frequently your tasks swap, the more likely you’ll encounter code that is missing a critical section or causes deadlock."
    timestamp = "12:17"
    type = MessageType.SENT
}


val message6 = ChatMessage().apply {
    text = "Android app facilitates long-range P2P communication between Android devices without cellular or WiFi. It employs Low Energy Bluetooth to pair with an Arduino acting as a LoRaWAN transceiVER"
    timestamp = "14:17"
    type = MessageType.RECEIVED
}

val message7 = ChatMessage().apply {
    text = "yeah"
    timestamp = "19:00"
    type = MessageType.SENT
}

// These sample messages are used only for development purposes
val chatBubbles = mutableListOf(message1, message2, message3, message3a
    ,message3b, message4, message5, message6, message7)