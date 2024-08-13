package com.lora.skylink.presentation.common

import com.lora.skylink.data.model.WirelessDevice
import kotlinx.coroutines.flow.MutableStateFlow

val mockWirelessDevice = WirelessDevice("GhostStar", "aa:bb:cc:dd", -99)
val mockWirelessDevice2 = WirelessDevice("PhantomStar", "ee:ff:aa:bb", -88)
val mockDevices = listOf(mockWirelessDevice, mockWirelessDevice2)
val mockDeviceStateFlow = MutableStateFlow<List<WirelessDevice>>(mockDevices)