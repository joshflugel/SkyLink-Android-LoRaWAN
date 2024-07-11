package com.lora.skylink.di


import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import com.lora.skylink.data.remote.bluetooth.BluetoothAdapterManager
import com.lora.skylink.data.remote.bluetooth.BluetoothLowEnergyScanController
import com.lora.skylink.data.remote.bluetoothlowenergy.BleConnectionManager
import com.lora.skylink.data.repositories.BluetoothConnectivityRepositoryImpl
import com.lora.skylink.data.repositories.BluetoothLowEnergyRepositoryImpl
import com.lora.skylink.domain.BluetoothDeviceConverter
import com.lora.skylink.domain.BluetoothDeviceConverterImpl
import com.lora.skylink.domain.IBluetoothConnectivityRepository
import com.lora.skylink.domain.IBluetoothLowEnergyRepository
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
    ): BleConnectionManager {
        return BleConnectionManager(context, bluetoothAdapter)
    }

    @Provides
    @Singleton
    fun provideBluetoothConnectivityRepository(
        deviceConverter: BluetoothDeviceConverter,
        bleConnectionManager: BleConnectionManager
    ): IBluetoothConnectivityRepository {
        return BluetoothConnectivityRepositoryImpl(deviceConverter, bleConnectionManager)
    }



    @Provides
    @Singleton
    fun provideBluetoothLowEnergyRepository(
        bleScanController: BluetoothLowEnergyScanController,
        bleManager: BluetoothAdapterManager
    ): IBluetoothLowEnergyRepository = BluetoothLowEnergyRepositoryImpl(bleScanController, bleManager)

    @Provides
    @Singleton
    fun provideLogger(): Logger = AppLogger


    /*
    @Provides
    fun providePermissionsRequester(fragment: Fragment): PermissionsRequester {
        return PermissionsRequester(fragment)
    }
    */
}