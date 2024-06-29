package com.lora.skylink.data


import android.bluetooth.BluetoothDevice
import com.lora.skylink.App
import com.lora.skylink.bluetoothlegacy.ConnectionEventListener
import com.lora.skylink.bluetoothlegacy.ConnectionManager
import com.lora.skylink.common.loge
import com.lora.skylink.domain.IBluetoothConnectivityRepository
import javax.inject.Inject


class BluetoothConnectivityRepositoryImpl @Inject constructor(
   // @ApplicationContext private val context: Context
) : IBluetoothConnectivityRepository {
        private val connectionEventListener = ConnectionEventListener()

        override fun connectToDevice(device: BluetoothDevice) {
            loge("BT Repo connectToDevice")
            ConnectionManager.connect(device, App.applicationContext())
        }

        override fun disconnectFromDevice(device: BluetoothDevice) {
            loge("BT Repo DisconnectFromDevice")
            ConnectionManager.disconnectFromDevice(device)
        }

        override fun teardownConnection(device: BluetoothDevice) {
            loge("BT Repo Teardown")
            ConnectionManager.disconnectFromDevice(device)
        }

        override fun registerConnectionEventListener(listener: ConnectionEventListener) {
            loge("BT Repo REGISTER ConnectionEventListener")
            ConnectionManager.registerListener(listener)
        }

        override fun unregisterConnectionEventListener(listener: ConnectionEventListener) {
            loge("BT Repo UNREGISTER ConnectionEventListener")
            ConnectionManager.unregisterListener(listener)
        }
    }
