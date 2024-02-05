package com.lora.skylink.ui.common

import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

//val BLUETOOTH_PERMISSION_REQUEST_CODE = 200


/* FCKUP
class PermissionsRequester(private val context: Context, private val permissions: Array<String>) {
    private var onRequest: (Boolean) -> Unit = {}
    private val launcher =
        (context as? FragmentActivity)?.registerForActivityResult(ActivityResultContracts
            .RequestMultiplePermissions()) { isGrantedMap: Map<String, Boolean> ->
                onRequest(isGrantedMap.values.all { it })
        }

    suspend fun request(): Boolean =
        suspendCancellableCoroutine { continuation ->
            onRequest = {
                loge("rabbit hole")
                continuation.resume(it)
            }
            launcher?.launch(permissions)
        }
    fun checkAllPermissions(): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }


 */

class PermissionsRequester(val fragment: Fragment, private val permissions: Array<String> = requiredAppPermissions) {

    private var onRequest: (Boolean) -> Unit = {}
    val launcher =
        fragment.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { isGrantedMap: Map<String, Boolean> ->
            onRequest(isGrantedMap.values.all { it })
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