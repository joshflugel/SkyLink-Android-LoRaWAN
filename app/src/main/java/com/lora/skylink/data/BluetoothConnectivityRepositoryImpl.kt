package com.lora.skylink.data


import android.bluetooth.BluetoothDevice
import android.content.Context
import com.lora.skylink.bluetoothlegacy.ConnectionEventListener
import com.lora.skylink.bluetoothlegacy.ConnectionManager
import com.lora.skylink.domain.IBluetoothConnectivityRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

//class BluetoothConnectivityRepositoryImpl(private val context: Context) : IBluetoothConnectivityRepository {
class BluetoothConnectivityRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : IBluetoothConnectivityRepository {
        private val connectionEventListener = ConnectionEventListener()

        init {
            ConnectionManager.registerListener(connectionEventListener)
        }

        override fun connectToDevice(device: BluetoothDevice) {
            ConnectionManager.connect(device, context)
        }

        override fun disconnectFromDevice(device: BluetoothDevice) {
            ConnectionManager.teardownConnection(device)
        }

        override fun registerConnectionEventListener(listener: ConnectionEventListener) {
            connectionEventListener.apply {
                onConnectionSetupComplete = listener.onConnectionSetupComplete
                onDisconnect = listener.onDisconnect
                // other callbacks
            }
        }

        override fun unregisterConnectionEventListener(listener: ConnectionEventListener) {
            ConnectionManager.unregisterListener(listener)
        }
    }
