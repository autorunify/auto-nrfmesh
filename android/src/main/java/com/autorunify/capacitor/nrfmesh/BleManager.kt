package com.autorunify.capacitor.nrfmesh

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.ParcelUuid
import androidx.lifecycle.MutableLiveData
import com.autorunify.capacitor.nrfmesh.SystemAsyncCall.Companion.MESH_STATE_CONNECTED
import com.autorunify.capacitor.nrfmesh.SystemAsyncCall.Companion.MESH_STATE_DISCONNECTED
import com.getcapacitor.JSObject
import com.getcapacitor.PluginCall
import kotlinx.coroutines.delay
import no.nordicsemi.android.mesh.MeshManagerApi
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
import no.nordicsemi.android.support.v18.scanner.ScanCallback
import no.nordicsemi.android.support.v18.scanner.ScanFilter
import no.nordicsemi.android.support.v18.scanner.ScanRecord
import no.nordicsemi.android.support.v18.scanner.ScanResult
import no.nordicsemi.android.support.v18.scanner.ScanSettings
import java.util.UUID

class BleManager {
    private class StateReceiver : BroadcastReceiver {
        private val ble: BleManager

        constructor(bleManager: BleManager) {
            this.ble = bleManager
        }

        override fun onReceive(context: Context, intent: Intent) {
            val eventName = "state"
            val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
            when (state) {
                BluetoothAdapter.STATE_ON -> {
                    ble.notify.notifyListeners(eventName, JSObject().apply {
                        synchronized(ble.devices) {
                            ble.devices.clear()
                        }

                        ble.device = null
                        ble.isConnected.postValue(false)
                        put("action", "enabled")
                        put("state", true)
                    })
                }

                BluetoothAdapter.STATE_OFF -> {
                    ble.notify.notifyListeners(eventName, JSObject().apply {
                        ble.device = null
                        ble.isConnected.postValue(false)
                        put("action", "enabled")
                        put("state", false)
                    })
                }
            }

            when (intent.action) {
                BluetoothDevice.ACTION_ACL_CONNECTED -> {
                    ble.isConnected.postValue(true)
                    ble.notify.notifyListeners(eventName, JSObject().apply {
                        put("action", "connected")
                        put("state", true)
                    })
                    ble.async.emit(JSObject(), MESH_STATE_CONNECTED)
                }

                BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                    synchronized(ble.devices) {
                        ble.devices.clear()
                    }

                    ble.device = null
                    ble.isConnected.postValue(false)
                    ble.notify.notifyListeners(eventName, JSObject().apply {
                        put("action", "connected")
                        put("state", false)
                    })
                    ble.async.emit(JSObject(), MESH_STATE_DISCONNECTED)
                }
            }
        }
    }

    private class ScanReceiver : ScanCallback {
        private val ble: BleManager

        constructor(bleManager: BleManager) {
            this.ble = bleManager
        }

        override fun onScanResult(callbackType: Int, scanResult: ScanResult) {
            val device = MeshDevice(scanResult)
            val uuid = scanResult.scanRecord?.serviceUuids?.get(0)?.uuid

            if (uuid == MeshManagerApi.MESH_PROVISIONING_UUID) {
                if (device.scanRecord == null) return

                val result = device.scanResult
                val record = device.scanRecord
                if (record.bytes == null || record.serviceUuids == null) return

                synchronized(ble.devices) {
                    val service = getService(result, MeshManagerApi.MESH_PROVISIONING_UUID)
                    if (service == null || service.size < 18) return


                    ble.mesh.delNodeByService(service)
                    device.uuid = ble.mesh.uuidFromService(service).toString()
                    device.provisioned = false

                    val _device = ble.devices.find { v ->
                        v.address == device.address
                    }

                    if (_device == null) {
                        ble.devices.add(device)
                    } else if (_device.provisioned) {
                        ble.devices.remove(_device)
                    } else {
                    }
                }
            } else if (uuid == MeshManagerApi.MESH_PROXY_UUID) {
                if (device.scanRecord == null) return

                val result = device.scanResult
                val record = device.scanRecord
                if (record.bytes == null || record.serviceUuids == null) return

                synchronized(ble.devices) {
                    val service = getService(result, MeshManagerApi.MESH_PROXY_UUID) ?: return
                    if (!ble.mesh.isProvisionedByService(service)) return

                    device.provisioned = true

                    val _device = ble.devices.find { v ->
                        v.address == device.address
                    }

                    if (_device == null) {
                        ble.devices.add(device)
                    } else if (!_device.provisioned) {
                        ble.devices.remove(_device)
                    } else {
                    }
                }
            }
        }

        override fun onScanFailed(errorCode: Int) {
            ble.scan(false)
        }

        fun getService(scanResult: ScanResult, serviceUuid: UUID): ByteArray? {
            val scanRecord: ScanRecord? = scanResult.scanRecord
            if (scanRecord != null) {
                return scanRecord.getServiceData(ParcelUuid((serviceUuid)))
            }

            return null
        }
    }

    private val notify: NrfMeshPlugin
    private val mesh: MeshManager
    private val async: AsyncManager
    private lateinit var context: Context

    private lateinit var adapter: BluetoothAdapter
    private lateinit var stateReceiver: StateReceiver
    private lateinit var scanReceiver: ScanReceiver

    private var isSupported: Boolean = false
    private var isScanning: Boolean = false

    val devices: MutableList<MeshDevice> = mutableListOf()
    var device: MeshDevice? = null
        private set
    val isConnected = MutableLiveData<Boolean>(false)

    constructor(asyncCalls: AsyncManager, notify: NrfMeshPlugin, meshManager: MeshManager) {
        this.notify = notify
        this.mesh = meshManager
        this.async = asyncCalls
    }

    fun load(activity: Activity, context: Context) {
        this.isSupported =
            activity.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
        if (!this.isSupported) return

        val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        this.adapter = manager.adapter
        this.context = context

        this.stateReceiver = StateReceiver(this)
        this.scanReceiver = ScanReceiver(this)
    }

    fun handleOnStart(context: Context) {

    }

    fun handleOnStop(context: Context) {
        this.scan(false)

        if (isConnected.value!!) {
            disconnect()
        }
    }

    fun assertFeature(call: PluginCall): Boolean {
        if (!isSupported) {
            call.reject("BLE is not supported.")
            return false
        }

        return true
    }

    fun assertAdapter(call: PluginCall): Boolean {
        if (adapter == null) {
            call.reject("BLE is not available.")
            return false
        }

        return true
    }

    fun assertEnabled(call: PluginCall): Boolean {
        if (adapter != null && adapter.isEnabled) {
            return true
        }

        call.reject("BLE is not enabled")
        return false
    }

    fun isEnabled(): Boolean {
        if (adapter != null) return adapter.isEnabled
        return false
    }

    fun scan(startScan: Boolean) {
        if (startScan) {
            try {
                val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
                filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
                filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
                context.registerReceiver(this.stateReceiver, filter)
            } catch (ex: Exception) {
            }


            val settings = ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setReportDelay(0)
                .setUseHardwareFilteringIfSupported(false)
                .build()

            val filters: MutableList<ScanFilter> = ArrayList()
            filters.add(
                ScanFilter.Builder()
                    .setServiceUuid(ParcelUuid(MeshManagerApi.MESH_PROVISIONING_UUID)).build()
            )
            filters.add(
                ScanFilter.Builder().setServiceUuid(ParcelUuid(MeshManagerApi.MESH_PROXY_UUID))
                    .build()
            )

            synchronized(this) {
                if (isScanning) return
                isScanning = true

                val scanner = BluetoothLeScannerCompat.getScanner()
                scanner.startScan(filters, settings, scanReceiver)
            }
        } else {
            synchronized(this) {
                if (!isScanning) return
                isScanning = false
                try {
                    context.unregisterReceiver(this.stateReceiver)
                } catch (ex: Exception) {
                }


                val scanner = BluetoothLeScannerCompat.getScanner()
                scanner.stopScan(scanReceiver)
            }
        }
    }

    suspend fun devicesWithFilter(filter: String, max: Int, timeout: Int): List<MeshDevice> {
        this.scan(false)

        synchronized(devices) {
            devices.clear()
        }

        this.scan(true)

        val timeMillis = 200L
        var timeCount = timeout.toLong() / timeMillis
        var devicesByFilter: MutableList<MeshDevice> = mutableListOf()

        while (timeCount > 0) {
            delay(timeMillis)
            devicesByFilter.clear()
            synchronized(devices) {
                devices.forEach { device ->
                    if (filter == "provisioned" && device.provisioned) {
                        devicesByFilter.add(device)
                    } else if (filter == "unprovisioned" && !device.provisioned) {
                        devicesByFilter.add(device)
                    } else if (filter == "all") {
                        devicesByFilter.add(device)
                    }
                }
            }

            if (max > 0 && devicesByFilter.size >= max) {
                break
            } else {
                timeCount--;
            }
        }

        return devicesByFilter
    }

    fun disconnect(): Boolean {
        try {
            if (isConnected.value == true) {
                mesh.disconnect()
            } else {
                async.emit(JSObject(), MESH_STATE_DISCONNECTED)
            }

            return isConnected.value!!
        } catch (ex: Exception) {

            async.emit(JSObject(), MESH_STATE_DISCONNECTED)
            return false
        }
    }

    private fun connect(device: MeshDevice?): Boolean {
        if (device == null) return false
        try {
            mesh.connect(device)
            this.device = device
        } catch (ex: Exception) {
            this.device = null
            return false
        }

        return isConnected.value!!
    }

    suspend fun connectToUnprovisioned(address: String, uuid: String): Boolean {
        if (isConnected.value == true) {
            if (device?.provisioned == false && device?.address == address) {
                return true
            } else {
                disconnect()
            }
        } else {
            if (mesh.isConnected) {
                disconnect()
            }
        }

        val maxRetry = 20;
        var retry = 0;

        while (retry < maxRetry) {
            retry++;

            if (devices.isEmpty()) {
                delay(500)
                continue
            }

            val _devices = devices.filter { !it.provisioned }.toMutableList()
            if (_devices.isEmpty()) {
                delay(500)
                continue
            }


            _devices.sortBy { device -> device.rssi }
            return connect(_devices.firstOrNull {
                it.let {
                    val service = scanReceiver.getService(
                        it.scanResult,
                        MeshManagerApi.MESH_PROVISIONING_UUID
                    )
                    val deviceUUID = mesh.uuidFromService(service!!)
                    deviceUUID.toString() == uuid
                }
            })
        }

        return false
    }

    suspend fun connectToProvisioned(address: String? = null): Boolean {
        if (isConnected.value == true || mesh.isConnected) {
            if (device?.provisioned == true && device?.address == mesh.device?.address) {
                return true
            } else {
                disconnect()
            }
        } else {
            if (mesh.isConnected) {
                disconnect()
            }
        }

        val maxRetry = 20;
        var retry = 0;

        while (retry < maxRetry) {
            retry++;

            if (devices.isEmpty()) {
                delay(500)
                continue
            }

            val _devices = devices.filter { it.provisioned }.toMutableList()
            if (_devices.isEmpty()) {
                delay(500)
                continue
            }

            if (address != null) {
                val device = _devices.firstOrNull { device -> device.address == address }
                if (device == null) {
                    delay(500)
                    continue
                }

                if (connect(device)) {
                    mesh.addFilterToProxy()
                    return true
                }
            } else {
                _devices.sortBy { device -> device.rssi }
                if (connect(_devices.firstOrNull())) {
                    mesh.addFilterToProxy()
                    return true
                }
            }
        }

        return false
    }
}