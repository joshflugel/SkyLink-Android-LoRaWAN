package com.lora.skylink.domain

import com.lora.skylink.data.BluetoothLowEnergyRepositoryImpl
import javax.inject.Inject

class CheckBluetoothEnabledUseCase @Inject constructor(
    private val bleRepository: BluetoothLowEnergyRepositoryImpl
) {
    operator fun invoke(): Boolean {
        return bleRepository.isBluetoothAdapterEnabled()
    }
}
