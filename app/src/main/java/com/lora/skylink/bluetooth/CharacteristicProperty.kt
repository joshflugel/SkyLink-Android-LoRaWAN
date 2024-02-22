package com.lora.skylink.bluetooth

enum class CharacteristicProperty {
    Readable,
    Writable,
    WritableWithoutResponse,
    Notifiable,
    Indicatable;

    val action
        get() = when (this) {
            Readable -> "Read"
            Writable -> "Write"
            WritableWithoutResponse -> "Write Without Response"
            Notifiable -> "Toggle Notifications"
            Indicatable -> "Toggle Indications"
        }
}