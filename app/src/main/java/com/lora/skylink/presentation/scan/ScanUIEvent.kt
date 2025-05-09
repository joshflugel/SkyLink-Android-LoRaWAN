package com.lora.skylink.presentation.scan

import android.bluetooth.BluetoothDevice

sealed class ScanUIEvent {
    data class NavigateToChat(val bluetoothDevice: BluetoothDevice) : ScanUIEvent()
    object NavigateToPermissions : ScanUIEvent()
    data class ShowError(val message: String) : ScanUIEvent()
}
