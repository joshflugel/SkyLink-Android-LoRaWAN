package com.lora.skylink.presentation.permissions

import androidx.lifecycle.ViewModel
import com.lora.skylink.domain.interfaces.BluetoothReadyChecker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class NumberOfPermissionRequestRetries(var value: Int = 0)

@HiltViewModel
class PermissionsViewModel @Inject constructor(
    private val bluetoothReadyChecker: BluetoothReadyChecker
) : ViewModel() {

    private val _permissionRequestRetries = MutableStateFlow(0)
    val permissionRequestRetries: StateFlow<Int>get() = _permissionRequestRetries

    var retries = NumberOfPermissionRequestRetries()
    fun incrementPermissionRequestRetries() {
        retries = retries.copy(value = retries.value + 1)
        _permissionRequestRetries.update { retries.value}
    }

    fun userHasDeniedPermissions():Boolean {
        return (retries.value > 2)
    }

    fun isBluetoothAdapterReady(): Boolean {
        return bluetoothReadyChecker.isBluetoothAdapterReady()
    }
}