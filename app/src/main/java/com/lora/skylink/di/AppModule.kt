package com.lora.skylink.di


import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.fragment.app.Fragment
import com.lora.skylink.data.BluetoothConnectivityRepositoryImpl
import com.lora.skylink.data.BluetoothLowEnergyRepositoryImpl
import com.lora.skylink.data.bluetoothLowEnergy.BluetoothAdapterManager
import com.lora.skylink.data.bluetoothLowEnergy.BluetoothLowEnergyScanController
import com.lora.skylink.domain.IBluetoothConnectivityRepository
import com.lora.skylink.domain.IBluetoothLowEnergyRepository
import com.lora.skylink.presentation.common.PermissionsRequester
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
@HiltAndroidApp
class AppModule: Application() {

    @Provides
    fun provideBluetoothAdapter(
        @ApplicationContext context: Context
    ): BluetoothAdapter {
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        return bluetoothManager.adapter
    }


    @Provides
    @Singleton
    fun provideBluetoothRepository(
        bleScanController: BluetoothLowEnergyScanController,
        bleManager: BluetoothAdapterManager
    ): IBluetoothLowEnergyRepository = BluetoothLowEnergyRepositoryImpl(bleScanController, bleManager)


    @Provides
    @Singleton
    fun provideBluetoothConnectivityRepository(
        @ApplicationContext context: Context
    ): IBluetoothConnectivityRepository = BluetoothConnectivityRepositoryImpl(context)


    @Provides
    fun providePermissionsRequester(fragment: Fragment): PermissionsRequester {
        return PermissionsRequester(fragment)
    }
}