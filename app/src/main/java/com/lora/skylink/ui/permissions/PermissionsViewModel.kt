package com.lora.skylink.ui.permissions

import androidx.lifecycle.ViewModel

data class NumberOfPermissionRequestRetries(var value: Int = 0)

class PermissionsViewModel : ViewModel() {
    var retries = NumberOfPermissionRequestRetries()
    fun incrementPermissionRequestRetries() {
       retries = retries.copy(value = retries.value + 1)
    }

    fun userHasDeniedPermissions():Boolean {
        return (retries.value > 2)
    }
}