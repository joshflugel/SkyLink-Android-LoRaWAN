package com.lora.skylink.ui.common

import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume



class PermissionsRequester(private val fragment: Fragment, private val permissions: Array<String> = requiredAppPermissions) {

  //  private lateinit var fragment: Fragment
    private var onRequest: (Boolean) -> Unit = {}
    var launcher = fragment.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { isGrantedMap: Map<String, Boolean> ->
        onRequest(isGrantedMap.values.all { it })
    }

    fun init(fragment: Fragment) {
      //  this.fragment = fragment
        this.launcher = fragment.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { isGrantedMap: Map<String, Boolean> ->
            onRequest(isGrantedMap.values.all { it })
        }
    }

    suspend fun request(): Boolean =
        suspendCancellableCoroutine { continuation ->
            onRequest = {
                continuation.resume(it)
            }
            launcher.launch(permissions)
        }

    fun checkAllPermissions(): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(fragment.requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }
}