package com.lora.skylink.common

/* todo
fun Parcelable.convertToScanResult(parcelable: Parcelable): ScanResult? {
    if (parcelable is ScanResult) { // Replace YourParcelableType with the actual type of your Parcelable object
        val yourParcelable:ScanResult = parcelable as ScanResult
        val deviceAddress = yourParcelable.deviceAddress // Extract device address from Parcelable
        val rssi = yourParcelable.rssi // Extract RSSI from Parcelable
        // Extract any other necessary information

        // Create ScanResult object
        val scanResult = ScanResult(null, null, rssi, 0, null, 0, 0, 0, false, deviceAddress, null)
        return scanResult
    }
    return null
}

 */