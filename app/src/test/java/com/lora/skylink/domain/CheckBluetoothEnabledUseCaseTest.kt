package com.lora.skylink.domain

import com.lora.skylink.domain.interfaces.IBluetoothLowEnergyRepository
import com.lora.skylink.domain.usecases.CheckBluetoothEnabledUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test


class CheckBluetoothEnabledUseCaseTest {

    @MockK
    private lateinit var bleRepository: IBluetoothLowEnergyRepository

    private lateinit var checkBluetoothEnabledUseCase: CheckBluetoothEnabledUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        checkBluetoothEnabledUseCase = CheckBluetoothEnabledUseCase(bleRepository)
    }

    @Test
    fun `useCase should return true when Bluetooth is enabled`() {
        every { bleRepository.isBluetoothAdapterReady() } returns true

        val result = checkBluetoothEnabledUseCase()

        assert(result)
        verify { bleRepository.isBluetoothAdapterReady() }
    }

    @Test
    fun `useCase should return false when Bluetooth is disabled`() {
        every { bleRepository.isBluetoothAdapterReady() } returns false

        val result = checkBluetoothEnabledUseCase()

        assert(!result)
        verify { bleRepository.isBluetoothAdapterReady() }
    }
}
