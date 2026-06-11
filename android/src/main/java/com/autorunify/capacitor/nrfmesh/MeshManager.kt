package com.autorunify.capacitor.nrfmesh

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.autorunify.capacitor.nrfmesh.SystemAsyncCall.Companion.MESH_NETWORK_INIT
import com.autorunify.capacitor.nrfmesh.SystemAsyncCall.Companion.MESH_NODE_IDENTIFY
import com.autorunify.capacitor.nrfmesh.SystemAsyncCall.Companion.MESH_NODE_PROVISION
import com.getcapacitor.JSObject
import com.getcapacitor.PluginCall
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.nordicsemi.android.ble.BleManager
import no.nordicsemi.android.mesh.ApplicationKey
import no.nordicsemi.android.mesh.Group
import no.nordicsemi.android.mesh.MeshManagerApi
import no.nordicsemi.android.mesh.MeshManagerCallbacks
import no.nordicsemi.android.mesh.MeshNetwork
import no.nordicsemi.android.mesh.MeshProvisioningStatusCallbacks
import no.nordicsemi.android.mesh.MeshStatusCallbacks
import no.nordicsemi.android.mesh.NetworkKey
import no.nordicsemi.android.mesh.provisionerstates.ProvisioningState
import no.nordicsemi.android.mesh.provisionerstates.UnprovisionedMeshNode
import no.nordicsemi.android.mesh.transport.ConfigAppKeyList
import no.nordicsemi.android.mesh.transport.ConfigAppKeyStatus
import no.nordicsemi.android.mesh.transport.ConfigCompositionDataStatus
import no.nordicsemi.android.mesh.transport.ConfigDefaultTtlStatus
import no.nordicsemi.android.mesh.transport.ConfigHeartbeatPublicationStatus
import no.nordicsemi.android.mesh.transport.ConfigHeartbeatSubscriptionStatus
import no.nordicsemi.android.mesh.transport.ConfigModelAppStatus
import no.nordicsemi.android.mesh.transport.ConfigNetKeyList
import no.nordicsemi.android.mesh.transport.ConfigNetKeyStatus
import no.nordicsemi.android.mesh.transport.ConfigNetworkTransmitStatus
import no.nordicsemi.android.mesh.transport.ConfigSigModelAppList
import no.nordicsemi.android.mesh.transport.ConfigStatusMessage
import no.nordicsemi.android.mesh.transport.ConfigVendorModelAppList
import no.nordicsemi.android.mesh.transport.ControlMessage
import no.nordicsemi.android.mesh.transport.GenericOnOffStatus
import no.nordicsemi.android.mesh.transport.MeshMessage
import no.nordicsemi.android.mesh.transport.ProvisionedMeshNode
import no.nordicsemi.android.mesh.transport.ProxyConfigAddAddressToFilter
import no.nordicsemi.android.mesh.transport.SensorSettingStatus
import no.nordicsemi.android.mesh.transport.SensorSettingsStatus
import no.nordicsemi.android.mesh.transport.SensorStatus
import no.nordicsemi.android.mesh.utils.AddressArray
import no.nordicsemi.android.mesh.utils.MeshParserUtils
import java.util.UUID

class MeshManager {
    private class MeshReceiver : MeshStatusCallbacks, MeshProvisioningStatusCallbacks,
        MeshManagerCallbacks {
        private val mesh: MeshManager

        constructor(mesh: MeshManager) {
            this.mesh = mesh
        }

        override fun onTransactionFailed(dst: Int, hasIncompleteTimerExpired: Boolean) {
        }

        override fun onUnknownPduReceived(src: Int, accessPayload: ByteArray?) {
        }

        override fun onBlockAcknowledgementProcessed(dst: Int, msg: ControlMessage) {
        }

        override fun onBlockAcknowledgementReceived(src: Int, msg: ControlMessage) {
        }

        override fun onHeartbeatMessageReceived(src: Int, msg: ControlMessage) {
            Log.i("NrfMesh", "onHeartbeatMessageReceived")
            mesh.notify.notifyListeners("node", JSObject().apply {
                put("action", "heartbeat")
                put("unicastAddress", src)
            })
        }

        override fun onMeshMessageProcessed(dst: Int, msg: MeshMessage) {
        }

        override fun onMeshMessageReceived(address: Int, msg: MeshMessage) {
            Log.i("NrfMesh", "onMeshMessageReceived")

            if (msg is ConfigStatusMessage) {
                if (msg.statusCode != 0) {
                    return mesh.async.error(mesh.formatter.toError(msg), msg.opCode, address)
                }
            }

            when (msg) {
                is ConfigCompositionDataStatus -> mesh.async.emit(
                    mesh.formatter.toJSON(msg),
                    msg.opCode,
                    address
                )

                is ConfigDefaultTtlStatus -> mesh.async.emit(
                    mesh.formatter.toJSON(msg),
                    msg.opCode,
                    address
                )

                is ConfigNetworkTransmitStatus -> mesh.async.emit(
                    mesh.formatter.toJSON(msg),
                    msg.opCode,
                    address
                )

                is ConfigNetKeyStatus -> mesh.async.emit(
                    mesh.formatter.toJSON(msg),
                    msg.opCode,
                    address
                )

                is ConfigNetKeyList -> mesh.async.emit(
                    mesh.formatter.toJSON(msg),
                    msg.opCode,
                    address
                )

                is ConfigAppKeyStatus -> mesh.async.emit(
                    mesh.formatter.toJSON(msg),
                    msg.opCode,
                    address
                )

                is ConfigAppKeyList -> mesh.async.emit(
                    mesh.formatter.toJSON(msg),
                    msg.opCode,
                    address
                )

                is ConfigHeartbeatPublicationStatus -> mesh.async.emit(
                    mesh.formatter.toJSON(msg),
                    msg.opCode,
                    address
                )

                is ConfigHeartbeatSubscriptionStatus -> mesh.async.emit(
                    mesh.formatter.toJSON(msg),
                    msg.opCode,
                    address
                )

                is ConfigModelAppStatus -> mesh.async.emit(
                    mesh.formatter.toJSON(msg),
                    msg.opCode,
                    address
                )

                is ConfigSigModelAppList -> mesh.async.emit(
                    mesh.formatter.toJSON(msg),
                    msg.opCode,
                    address
                )

                is ConfigVendorModelAppList -> mesh.async.emit(
                    mesh.formatter.toJSON(msg),
                    msg.opCode,
                    address
                )

                is GenericOnOffStatus -> mesh.async.emit(
                    mesh.formatter.toJSON(msg),
                    msg.opCode,
                    address
                )

                is SensorStatus-> mesh.async.emit(
                    mesh.formatter.toJSON(msg),
                    msg.opCode,
                    address
                )

                is SensorSettingStatus -> mesh.async.emit(
                    mesh.formatter.toJSON(msg),
                    msg.opCode,
                    address
                )

                is SensorSettingsStatus -> mesh.async.emit(
                    mesh.formatter.toJSON(msg),
                    msg.opCode,
                    address
                )
            }
        }

        override fun onMessageDecryptionFailed(layer: String?, error: String?) {
        }

        override fun onProvisioningStateChanged(
            meshNode: UnprovisionedMeshNode?, state: ProvisioningState.States?, data: ByteArray?
        ) {
            if (state == ProvisioningState.States.PROVISIONING_CAPABILITIES) {
                mesh.unodes.add(meshNode!!)
                mesh.async.emit(mesh.formatter.toJSON(meshNode), MESH_NODE_IDENTIFY)
            }
        }

        override fun onProvisioningFailed(
            meshNode: UnprovisionedMeshNode?, state: ProvisioningState.States?, data: ByteArray?
        ) {
            if (state == ProvisioningState.States.PROVISIONING_FAILED) {
                mesh.unodes.remove(meshNode!!)
                mesh.async.emit(JSObject().apply {
                    put("completed", false)
                    put("uuid", meshNode.deviceUuid)
                }, MESH_NODE_PROVISION)
            }
        }

        @SuppressLint("RestrictedApi")
        override fun onProvisioningCompleted(
            meshNode: ProvisionedMeshNode?, state: ProvisioningState.States?, data: ByteArray?
        ) {
            if (state == ProvisioningState.States.PROVISIONING_COMPLETE) {
                val unode = mesh.unodes.find { node ->
                    if (node.deviceKey == null) return@find false

                    val ukey = MeshParserUtils.bytesToHex(node.deviceKey, false)
                    val mkey = MeshParserUtils.bytesToHex(meshNode!!.deviceKey, false)

                    return@find ukey == mkey
                }
                if (unode != null) mesh.unodes.remove(unode)


                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        mesh.disconnect()
                    } catch (ex: Exception) {
                    }
                }
                meshNode!!.nodeName = meshNode.unicastAddress.toString(16).padStart(4, '0')

                mesh.async.emit(JSObject().apply {
                    put("completed", true)
                    put("uuid", meshNode.uuid)
                    put("unicastAddress", meshNode.unicastAddress)
                }, MESH_NODE_PROVISION)

                mesh.notify.notifyListeners("node", JSObject().apply {
                    put("action", "add")
                    put("unicastAddress", meshNode.unicastAddress)
                })
            }
        }

        override fun onNetworkLoaded(meshNetwork: MeshNetwork?) {
            if (meshNetwork == null) return

            synchronized(mesh) {
                if (!meshNetwork.isProvisionerSelected) {
                    val provisioner = meshNetwork.provisioners[0]
                    meshNetwork.selectProvisioner(provisioner)
                }

                if (meshNetwork.appKeys.isEmpty()) {
                    val defaultAppKey = meshNetwork.createAppKey()
                    meshNetwork.addAppKey(defaultAppKey)
                }
            }

            mesh.async.emit(JSObject(mesh.api.exportMeshNetwork()), MESH_NETWORK_INIT)
        }

        override fun onNetworkUpdated(meshNetwork: MeshNetwork?) {
        }

        override fun onNetworkLoadFailed(error: String?) {
        }

        override fun onNetworkImported(meshNetwork: MeshNetwork?) {
        }

        override fun onNetworkImportFailed(error: String?) {
        }

        override fun sendProvisioningPdu(
            meshNode: UnprovisionedMeshNode?, pdu: ByteArray?
        ) {
            mesh.gatt.writeTo(pdu)
        }

        override fun onMeshPduCreated(pdu: ByteArray?) {
            mesh.gatt.writeTo(pdu)
        }

        override fun getMtu(): Int {
            return mesh.gatt.mtu - 3
        }
    }

    private class GattManager : BleManager {
        private var dataInCharacteristic: BluetoothGattCharacteristic? = null
        private var dataOutCharacteristic: BluetoothGattCharacteristic? = null
        val isReady = MutableLiveData<Boolean>(false)

        private val mesh: MeshManager

        constructor(context: Context, mesh: MeshManager) : super(context) {
            this.mesh = mesh
        }

        override fun isRequiredServiceSupported(gatt: BluetoothGatt): Boolean {
            val proxyService = gatt.getService(MeshManagerApi.MESH_PROXY_UUID)
            if (proxyService != null) {
                dataInCharacteristic =
                    proxyService.getCharacteristic(UUID.fromString("00002ADD-0000-1000-8000-00805F9B34FB"))
                dataOutCharacteristic =
                    proxyService.getCharacteristic(UUID.fromString("00002ADE-0000-1000-8000-00805F9B34FB"))

                return this.verifyCharacteristic()
            }

            val provisioningService = gatt.getService(MeshManagerApi.MESH_PROVISIONING_UUID)
            if (provisioningService != null) {
                dataInCharacteristic =
                    provisioningService.getCharacteristic(UUID.fromString("00002ADB-0000-1000-8000-00805F9B34FB"))
                dataOutCharacteristic =
                    provisioningService.getCharacteristic(UUID.fromString("00002ADC-0000-1000-8000-00805F9B34FB"))

                return this.verifyCharacteristic()
            }

            return false
        }

        override fun initialize() {
            this.requestMtu(517).enqueue()

            this.setNotificationCallback(dataOutCharacteristic).with { device, data ->
                mesh.api.handleNotifications(mtu - 3, data.value!!)
            }
            this.enableNotifications(dataOutCharacteristic).enqueue()
        }

        override fun onDeviceReady() {
            isReady.postValue(true)
        }

        override fun onServicesInvalidated() {
            overrideMtu(23)
            isReady.postValue(false)
            dataInCharacteristic = null
            dataOutCharacteristic = null
        }

        override fun shouldClearCacheWhenDisconnected(): Boolean {
            return true
        }

        private fun verifyCharacteristic(): Boolean {
            if (dataInCharacteristic == null) return false
            if (dataOutCharacteristic == null) return false
            if (dataOutCharacteristic!!.properties.and(BluetoothGattCharacteristic.PROPERTY_NOTIFY) == 0) return false
            if (dataInCharacteristic!!.properties.and(BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) == 0) return false
            return true
        }

        public override fun getMtu(): Int {
            return super.getMtu()
        }

        fun writeTo(data: ByteArray?) {
            if (isReady.value != true) return

            val writeType = BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE
            writeCharacteristic(dataInCharacteristic, data, writeType).split()
                .with { device, data ->
                    mesh.api.handleWriteCallbacks(mtu - 3, data.value!!)
                }.enqueue()
        }
    }

    val formatter: FormatManager
    private val unodes: MutableList<UnprovisionedMeshNode>
    private val async: AsyncManager
    private val notify: NrfMeshPlugin
    private val receiver: MeshReceiver
    private lateinit var api: MeshManagerApi
    private lateinit var gatt: GattManager


    val device
        get() = gatt.bluetoothDevice
    val isConnected
        get() = gatt.isConnected

    val netkeys
        get() = api.meshNetwork!!.netKeys
    val appkeys
        get() = api.meshNetwork!!.appKeys
    val groups
        get() = api.meshNetwork!!.groups
    val nodes
        get() = api.meshNetwork!!.nodes


    constructor(asyncCalls: AsyncManager, notify: NrfMeshPlugin) {
        this.async = asyncCalls
        this.notify = notify
        this.unodes = mutableListOf<UnprovisionedMeshNode>()
        this.receiver = MeshReceiver(this)
        this.formatter = FormatManager()
    }

    fun assertNetwork(call: PluginCall): Boolean {
        if (api.meshNetwork == null) {
            call.reject("mesh network not init.")
            return false
        }

        return true
    }

    fun load(activity: Activity, context: Context) {
        this.api = MeshManagerApi(context)
        this.gatt = GattManager(context, this)

        this.api.setMeshStatusCallbacks(this.receiver)
        this.api.setProvisioningStatusCallbacks(this.receiver)
        this.api.setMeshManagerCallbacks(this.receiver)
    }

    fun addFilterToProxy() {
        val message = ProxyConfigAddAddressToFilter(arrayListOf<AddressArray>().apply {
            add(AddressArray(0x00, 0x01))
        })

        api.createMeshPdu(0xFFFF, message)
    }

    fun createMeshPdu(address: Int, message: MeshMessage) {
        api.createMeshPdu(address, message)
    }

    fun uuidFromService(service: ByteArray): UUID {
        return api.getDeviceUuid(service)
    }

    fun delNodeByService(service: ByteArray) {
        api.meshNetwork?.nodes?.forEach { node ->
            val uuid = uuidFromService(service)
            if (node.uuid == uuid.toString()) {
                api.meshNetwork?.deleteNode(node)
                notify.notifyListeners("node", JSObject().apply {
                    put("action", "del")
                    put("unicastAddress", node.unicastAddress)
                })
            }
        }
    }

    fun isProvisionedByService(service: ByteArray): Boolean {
        if (api.isAdvertisingWithNetworkIdentity(service)) {
            if (api.meshNetwork != null && api.networkIdMatches(service)) {
                return true
            }
        } else if (api.isAdvertisedWithNodeIdentity(service)) {
            if (api.meshNetwork != null) {
                api.meshNetwork?.nodes?.forEach { node ->
                    if (api.nodeIdentityMatches(node, service)) {
                        return true
                    }
                }
            }
        }

        return false
    }

    fun disconnect() {
        gatt.disconnect().timeout(2000).await()
    }

    fun connect(device: MeshDevice) {
        gatt.connect(device.device).retry(20, 500).await()
    }

    fun export(): String? {
        return api.exportMeshNetwork()
    }

    fun import(network: String) {
        api.importMeshNetworkJson(network)
    }

    fun netkeyAdd(): NetworkKey {
        val network = api.meshNetwork!!
        val netKey = network.createNetworkKey()
        network.addNetKey(netKey)
        return netKey
    }

    fun netkeyDel(index: Int): NetworkKey? {
        val network = api.meshNetwork!!
        val netkey = network.getNetKey(index) ?: return null

        val appkeys = network.appKeys.filter { it.boundNetKeyIndex == netkey.keyIndex }
        if (!appkeys.isEmpty()) {
            appkeys.forEach { appkey ->
                network.removeAppKey(appkey)
            }
        }
        network.removeNetKey(netkey)
        return netkey
    }

    fun netkeyGet(index: Int): NetworkKey? {
        val network = api.meshNetwork!!
        return network.getNetKey(index)
    }

    fun appkeyAdd(boundNetKey: Int?): ApplicationKey {
        val network = api.meshNetwork!!
        val appkey = network.createAppKey()
        if (boundNetKey != null) {
            appkey.boundNetKeyIndex = boundNetKey
        }
        network.addAppKey(appkey)
        return appkey
    }

    fun appkeyDel(index: Int): ApplicationKey? {
        val network = api.meshNetwork!!
        val appkey = network.getAppKey(index) ?: return null
        network.removeAppKey(appkey)
        return appkey
    }

    fun appkeyGet(index: Int): ApplicationKey? {
        val network = api.meshNetwork!!
        return network.getAppKey(index)
    }

    fun groupAdd(address: Int): Group {
        val network = api.meshNetwork!!
        val provisioner = network.selectedProvisioner
        val name = "Group 0x" + address.toString(16).padStart(4, '0').uppercase()
        val group = network.createGroup(provisioner, address, name)
        network.addGroup(group)
        return group
    }

    fun groupDel(address: Int): Group? {
        val network = api.meshNetwork!!
        if (nodes.any { node ->
                node.elements.any { (i, element) ->
                    element.meshModels.any { (i, model) ->
                        model.subscribedAddresses.contains(
                            address
                        ) || model.publicationSettings.publishAddress == address
                    }
                }
            }) {
            throw Exception("group in used")
        }

        val group = network.getGroup(address) ?: return null
        network.removeGroup(group)
        return group
    }

    fun groupGet(address: Int): Group? {
        val network = api.meshNetwork!!
        return network.getGroup(address)
    }

    fun nodeGet(unicastAddress: Int): ProvisionedMeshNode? {
        val network = api.meshNetwork!!
        return network.getNode(unicastAddress)
    }

    fun init() {
        if (this.api.meshNetwork == null) {
            this.api.loadMeshNetwork()
        } else {
            this.async.emit(JSObject(api.exportMeshNetwork()), MESH_NETWORK_INIT)
        }
    }

    fun identify(uuid: UUID) {
        this.api.identifyNode(uuid)
    }

    fun provision(uuid: UUID) {
        val unode = unodes.find {
            it.deviceUuid == uuid
        }

        val provisioner = api.meshNetwork?.selectedProvisioner
        val unicastAddress = api.meshNetwork?.nextAvailableUnicastAddress(
            unode!!.numberOfElements, provisioner!!
        )

        unode!!.nodeName = unicastAddress.toString()

        api.meshNetwork!!.assignUnicastAddress(unicastAddress!!)
        api.startProvisioning(unode)
    }
}