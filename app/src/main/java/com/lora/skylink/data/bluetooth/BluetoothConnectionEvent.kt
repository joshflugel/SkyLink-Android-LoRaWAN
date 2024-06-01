package com.lora.skylink.data.bluetooth


import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor

sealed class BluetoothConnectionEvent {
    data class SetupComplete(val gatt: BluetoothGatt) : BluetoothConnectionEvent()
    data class Connected(val device: BluetoothDevice) : BluetoothConnectionEvent()
    data class Disconnected(val device: BluetoothDevice) : BluetoothConnectionEvent()
    data class CharacteristicRead(val device: BluetoothDevice, val characteristic: BluetoothGattCharacteristic) : BluetoothConnectionEvent()
    data class CharacteristicWrite(val device: BluetoothDevice, val characteristic: BluetoothGattCharacteristic) : BluetoothConnectionEvent()
    data class DescriptorRead(val device: BluetoothDevice, val descriptor: BluetoothGattDescriptor) : BluetoothConnectionEvent()
    data class DescriptorWrite(val device: BluetoothDevice, val descriptor: BluetoothGattDescriptor) : BluetoothConnectionEvent()
    data class NotificationsEnabled(val device: BluetoothDevice, val characteristic: BluetoothGattCharacteristic) : BluetoothConnectionEvent()
    data class NotificationsDisabled(val device: BluetoothDevice, val characteristic: BluetoothGattCharacteristic) : BluetoothConnectionEvent()
    data class MtuChanged(val device: BluetoothDevice, val mtu: Int) : BluetoothConnectionEvent()
}
