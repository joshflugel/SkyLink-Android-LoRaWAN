package com.lora.skylink.presentation.permissions

import androidx.lifecycle.ViewModel

data class NumberOfPermissionRequestRetries(var value: Int = 0)

class PermissionsViewModel : ViewModel() {
    private var retries = NumberOfPermissionRequestRetries()
    fun incrementPermissionRequestRetries() {
       retries = retries.copy(value = retries.value + 1)
    }

    fun userHasDeniedPermissions():Boolean {
        return (retries.value > 2)
    }
}