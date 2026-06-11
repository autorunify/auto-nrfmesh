package com.autorunify.capacitor.nrfmesh

import android.bluetooth.BluetoothDevice
import no.nordicsemi.android.support.v18.scanner.ScanRecord
import no.nordicsemi.android.support.v18.scanner.ScanResult

class MeshDevice {
    val scanResult: ScanResult
    val scanRecord: ScanRecord?

    val device: BluetoothDevice
    val name: String
    val rssi: Int

    val address: String
        get() = device.address

    var provisioned: Boolean = false
    var uuid: String? = null

    constructor(scanResult: ScanResult) {
        this.scanResult = scanResult
        this.device = scanResult.device
        this.rssi = scanResult.rssi

        this.scanRecord = scanResult.scanRecord
        this.name = if (scanRecord != null) scanRecord.deviceName!! else "Unknown"
    }

    fun match(scanResult: ScanResult): Boolean {
        return this.address == scanResult.device.address
    }

    override fun equals(other: Any?): Boolean {
        if (other is MeshDevice) {
            return device.address == other.device.address
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = device.hashCode() ?: 0
        result = 31 * result + (scanResult.hashCode() ?: 0)
        result = 31 * result + (name.hashCode() ?: 0)
        result = 31 * result + rssi
        return result
    }

}