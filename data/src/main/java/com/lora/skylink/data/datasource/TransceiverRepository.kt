package com.lora.skylink.data.datasource

import javax.inject.Inject



class TransceiverRepository @Inject constructor(
//    private val bluetoothLoraArduinoDataSource: BluetoothLoraArduinoDataSource
) {
    suspend fun getScannedBluetoothLoraArduinosList(): Unit? {

/*
        val scanBluetoothLoraDevices = bluetoothLoraArduinoDataSource.getScanneDevices()
        scanBluetoothLoraDevices.fold(isLeft + {return it }){
            loca
        }
*/
        return null
    }
}