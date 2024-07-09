package com.lora.skylink.domain

import javax.inject.Inject

class CheckBluetoothEnabledUseCase @Inject constructor(
    private val bleRepository: IBluetoothLowEnergyRepository
) {
    operator fun invoke(): Boolean {
        return bleRepository.isBluetoothAdapterEnabled()
    }
}
