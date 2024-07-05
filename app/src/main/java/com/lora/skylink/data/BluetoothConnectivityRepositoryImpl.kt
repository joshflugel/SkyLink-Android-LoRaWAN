package com.lora.skylink.data


import com.lora.skylink.App
import com.lora.skylink.bluetoothlegacy.ConnectionEventListener
import com.lora.skylink.bluetoothlegacy.ConnectionManager
import com.lora.skylink.util.loge
import com.lora.skylink.data.model.WirelessDevice
import com.lora.skylink.domain.BluetoothDeviceConverter
import com.lora.skylink.domain.IBluetoothConnectivityRepository
import javax.inject.Inject


class BluetoothConnectivityRepositoryImpl @Inject constructor(
    private val deviceConverter: BluetoothDeviceConverter
) : IBluetoothConnectivityRepository {
        private val connectionEventListener = ConnectionEventListener()

        override fun connectToDevice(device: WirelessDevice) {
            loge("BT Repo connectToDevice")
            val bluetoothDevice = deviceConverter.toBluetoothDevice(device)
            bluetoothDevice?.let {
                ConnectionManager.connect(it, App.applicationContext())
            }
        }

        override fun disconnectFromDevice(device: WirelessDevice) {
            loge("BT Repo DisconnectFromDevice")
            val bluetoothDevice = deviceConverter.toBluetoothDevice(device)
            bluetoothDevice?.let {
                ConnectionManager.disconnectFromDevice(it)
            }
        }

        override fun teardownConnection(device: WirelessDevice) {
            loge("BT Repo Teardown")
            val bluetoothDevice = deviceConverter.toBluetoothDevice(device)
            bluetoothDevice?.let {
                ConnectionManager.disconnectFromDeviceAndReleaseResources(it)
            }
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
