package com.lora.skylink.data.repositories


import com.lora.skylink.App
import com.lora.skylink.data.model.WirelessDevice
import com.lora.skylink.data.remote.bluetoothlowenergy.ConnectionEventListener
import com.lora.skylink.data.remote.bluetoothlowenergy.ConnectionManager
import com.lora.skylink.domain.BluetoothDeviceConverter
import com.lora.skylink.domain.IBluetoothConnectivityRepository
import com.lora.skylink.util.AppLogger.loge
import com.lora.skylink.util.AppLogger.logi
import javax.inject.Inject


class BluetoothConnectivityRepositoryImpl @Inject constructor(
    private val deviceConverter: BluetoothDeviceConverter,
    private val connectionManager: ConnectionManager
) : IBluetoothConnectivityRepository {

        override fun connectToDevice(device: WirelessDevice) {
            logi("BT Repo connectToDevice")
            val bluetoothDevice = deviceConverter.toBluetoothDevice(device)
            val context = App.applicationContext()

            bluetoothDevice?.let {
                connectionManager.connect(it, context)
            }
        }

        override fun disconnectFromDevice(device: WirelessDevice) {
            logi("BT Repo DisconnectFromDevice")
            val bluetoothDevice = deviceConverter.toBluetoothDevice(device)
            bluetoothDevice?.let {
                connectionManager.disconnectFromDevice(it)
            }
        }

        override fun teardownConnection(device: WirelessDevice) {
            loge("BT Repo Teardown")
            val bluetoothDevice = deviceConverter.toBluetoothDevice(device)
            bluetoothDevice?.let {
                connectionManager.disconnectFromDeviceAndReleaseResources(it)
            }
        }

        override fun registerConnectionEventListener(listener: ConnectionEventListener) {
            logi("BT Repo REGISTER ConnectionEventListener")
            connectionManager.registerListener(listener)
        }

        override fun unregisterConnectionEventListener(listener: ConnectionEventListener) {
            logi("BT Repo UNREGISTER ConnectionEventListener")
            connectionManager.unregisterListener(listener)
        }
    }
