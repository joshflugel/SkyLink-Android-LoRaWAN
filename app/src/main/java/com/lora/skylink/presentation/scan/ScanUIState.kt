package com.lora.skylink.presentation.scan

sealed class ScanUIState {
    object Idle : ScanUIState()
    object Scanning : ScanUIState()
    data class Error(val message: String) : ScanUIState()
}
