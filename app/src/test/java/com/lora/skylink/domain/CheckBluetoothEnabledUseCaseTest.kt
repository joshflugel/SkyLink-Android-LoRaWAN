package com.lora.skylink.domain

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
        every { bleRepository.isBluetoothAdapterEnabled() } returns true

        val result = checkBluetoothEnabledUseCase()

        assert(result)
        verify { bleRepository.isBluetoothAdapterEnabled() }
    }

    @Test
    fun `useCase should return false when Bluetooth is disabled`() {
        every { bleRepository.isBluetoothAdapterEnabled() } returns false

        val result = checkBluetoothEnabledUseCase()

        assert(!result)
        verify { bleRepository.isBluetoothAdapterEnabled() }
    }
}
