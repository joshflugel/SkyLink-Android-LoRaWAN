package com.lora.skylink.domain.usecases

import com.lora.skylink.domain.interfaces.IBluetoothLowEnergyRepository
import javax.inject.Inject

class CheckBluetoothEnabledUseCase @Inject constructor(
    private val bleRepository: IBluetoothLowEnergyRepository
) {
    operator fun invoke(): Boolean {
        return bleRepository.isBluetoothAdapterReady()
    }
}
