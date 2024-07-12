package com.lora.skylink.presentation.common

import androidx.fragment.app.Fragment
import javax.inject.Inject

class PermissionsRequesterFactory @Inject constructor(
    private val fragment: Fragment
) {
    fun create(): PermissionsRequester {
        return PermissionsRequester(fragment)
    }
}
