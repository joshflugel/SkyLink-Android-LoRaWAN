package com.lora.skylink.di


import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import com.lora.skylink.data.framework.bluetooth.communication.BleCommunicationsManager
import com.lora.skylink.data.framework.bluetooth.scanning.BluetoothLowEnergyScanController
import com.lora.skylink.data.framework.bluetooth.scanning.BluetoothReadyCheckerImpl
import com.lora.skylink.data.repositories.BluetoothConnectivityRepositoryImpl
import com.lora.skylink.data.repositories.BluetoothLowEnergyRepositoryImpl
import com.lora.skylink.domain.BluetoothDeviceConverter
import com.lora.skylink.domain.BluetoothDeviceConverterImpl
import com.lora.skylink.domain.interfaces.BluetoothReadyChecker
import com.lora.skylink.domain.interfaces.IBluetoothConnectivityRepository
import com.lora.skylink.domain.interfaces.IBluetoothLowEnergyRepository
import com.lora.skylink.util.AppLogger
import com.lora.skylink.util.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object Di{

    @Provides
    @Singleton
    fun provideBluetoothDeviceChecker(
        bluetoothAdapter: BluetoothAdapter
    ): BluetoothReadyChecker = BluetoothReadyCheckerImpl(bluetoothAdapter)

    @Provides
    fun provideBluetoothAdapter(
        @ApplicationContext context: Context
    ): BluetoothAdapter {
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        return bluetoothManager.adapter
    }

    @Provides
    @Singleton
    fun provideBluetoothDeviceConverter(
        bluetoothAdapter: BluetoothAdapter
    ): BluetoothDeviceConverter = BluetoothDeviceConverterImpl(bluetoothAdapter)

    @Provides
    @Singleton
    fun provideConnectionManager(
        @ApplicationContext context: Context,
        bluetoothAdapter: BluetoothAdapter
    ): BleCommunicationsManager {
        return BleCommunicationsManager(context, bluetoothAdapter)
    }

    @Provides
    @Singleton
    fun provideBluetoothConnectivityRepository(
        deviceConverter: BluetoothDeviceConverter,
        bleConnectionManager: BleCommunicationsManager
    ): IBluetoothConnectivityRepository {
        return BluetoothConnectivityRepositoryImpl(deviceConverter, bleConnectionManager)
    }

    @Provides
    @Singleton
    fun provideBluetoothLowEnergyRepository(
        bleScanController: BluetoothLowEnergyScanController,
        bleChecker: BluetoothReadyCheckerImpl
    ): IBluetoothLowEnergyRepository = BluetoothLowEnergyRepositoryImpl(bleScanController, bleChecker)

    @Provides
    @Singleton
    fun provideLogger(): Logger = AppLogger

}