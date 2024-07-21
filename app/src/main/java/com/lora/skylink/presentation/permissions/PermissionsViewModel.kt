package com.lora.skylink.presentation.permissions

import androidx.lifecycle.ViewModel
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class NumberOfPermissionRequestRetries(var value: Int = 0)

class PermissionsViewModel : ViewModel() {

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
}