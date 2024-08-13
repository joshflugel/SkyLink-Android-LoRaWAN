package com.lora.skylink.presentation.scan

import com.lora.skylink.domain.CheckBluetoothEnabledUseCase
import com.lora.skylink.domain.ManageBluetoothDeviceConnectionUseCase
import com.lora.skylink.domain.ScanBluetoothLowEnergyDevicesUseCase
import com.lora.skylink.presentation.common.MainDispatcherRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
abstract class BaseScanViewModelTest {

    @get:Rule
    val rule = MainDispatcherRule()

    protected val testScheduler = TestCoroutineScheduler()
    protected val testDispatcher = StandardTestDispatcher(testScheduler)
    protected val testScope = TestScope(testDispatcher)

    protected lateinit var scanBluetoothLeDevicesUseCase: ScanBluetoothLowEnergyDevicesUseCase
    protected lateinit var checkBluetoothEnabledUseCase: CheckBluetoothEnabledUseCase
    protected lateinit var manageBluetoothDeviceConnectionUseCase: ManageBluetoothDeviceConnectionUseCase
    protected lateinit var viewModel: ScanViewModel

    @Before
    open fun setUp() {
        Dispatchers.setMain(testDispatcher)
        scanBluetoothLeDevicesUseCase = mock(ScanBluetoothLowEnergyDevicesUseCase::class.java)
        checkBluetoothEnabledUseCase = mock(CheckBluetoothEnabledUseCase::class.java)
        manageBluetoothDeviceConnectionUseCase = mock(ManageBluetoothDeviceConnectionUseCase::class.java)
        viewModel = ScanViewModel(scanBluetoothLeDevicesUseCase, checkBluetoothEnabledUseCase, manageBluetoothDeviceConnectionUseCase)
    }
}
