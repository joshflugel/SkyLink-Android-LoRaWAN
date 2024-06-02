package com.lora.skylink.data


import android.bluetooth.BluetoothDevice
import com.lora.skylink.App
import com.lora.skylink.bluetoothlegacy.ConnectionEventListener
import com.lora.skylink.bluetoothlegacy.ConnectionManager
import com.lora.skylink.domain.IBluetoothConnectivityRepository
import javax.inject.Inject


class BluetoothConnectivityRepositoryImpl @Inject constructor(
   // @ApplicationContext private val context: Context
) : IBluetoothConnectivityRepository {
        private val connectionEventListener = ConnectionEventListener()

        init {
            ConnectionManager.registerListener(connectionEventListener)
        }

        override fun connectToDevice(device: BluetoothDevice) {  ConnectionManager.connect(device, App.applicationContext()) }
    /*
    override suspend fun connectToDevice(device: BluetoothDevice): Result<ConnectionResult> {
        return try {
            // Assume that bleManager.connect(device) is a suspend function
            val result = bleManager.connect(device)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
*/

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
