package com.autorunify.capacitor.nrfmesh

import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.autorunify.capacitor.nrfmesh.SystemAsyncCall.Companion.MESH_NETWORK_INIT
import com.autorunify.capacitor.nrfmesh.SystemAsyncCall.Companion.MESH_NODE_IDENTIFY
import com.autorunify.capacitor.nrfmesh.SystemAsyncCall.Companion.MESH_NODE_PROVISION
import com.autorunify.capacitor.nrfmesh.SystemAsyncCall.Companion.MESH_STATE_DISCONNECTED
import com.getcapacitor.JSArray
import com.getcapacitor.JSObject
import com.getcapacitor.PermissionState
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.CapacitorPlugin
import com.getcapacitor.annotation.Permission
import com.getcapacitor.annotation.PermissionCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.nordicsemi.android.mesh.ApplicationKey
import no.nordicsemi.android.mesh.Features
import no.nordicsemi.android.mesh.Group
import no.nordicsemi.android.mesh.NetworkKey
import no.nordicsemi.android.mesh.models.SigModel
import no.nordicsemi.android.mesh.opcodes.ApplicationMessageOpCodes.GENERIC_ON_OFF_GET
import no.nordicsemi.android.mesh.opcodes.ApplicationMessageOpCodes.GENERIC_ON_OFF_SET
import no.nordicsemi.android.mesh.opcodes.ApplicationMessageOpCodes.SENSOR_GET
import no.nordicsemi.android.mesh.opcodes.ApplicationMessageOpCodes.SENSOR_SETTINGS_GET
import no.nordicsemi.android.mesh.opcodes.ApplicationMessageOpCodes.SENSOR_SETTING_GET
import no.nordicsemi.android.mesh.opcodes.ApplicationMessageOpCodes.SENSOR_SETTING_SET
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_APPKEY_ADD
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_APPKEY_DELETE
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_APPKEY_GET
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_COMPOSITION_DATA_GET
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_DEFAULT_TTL_SET
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_HEARTBEAT_PUBLICATION_GET
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_HEARTBEAT_PUBLICATION_SET
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_HEARTBEAT_SUBSCRIPTION_GET
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_HEARTBEAT_SUBSCRIPTION_SET
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_MODEL_APP_BIND
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_MODEL_APP_UNBIND
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_NETKEY_ADD
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_NETKEY_DELETE
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_NETKEY_GET
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_NETWORK_TRANSMIT_GET
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_NETWORK_TRANSMIT_SET
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_SIG_MODEL_APP_GET
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_VENDOR_MODEL_APP_GET
import no.nordicsemi.android.mesh.transport.ConfigAppKeyAdd
import no.nordicsemi.android.mesh.transport.ConfigAppKeyDelete
import no.nordicsemi.android.mesh.transport.ConfigAppKeyGet
import no.nordicsemi.android.mesh.transport.ConfigCompositionDataGet
import no.nordicsemi.android.mesh.transport.ConfigDefaultTtlGet
import no.nordicsemi.android.mesh.transport.ConfigDefaultTtlSet
import no.nordicsemi.android.mesh.transport.ConfigHeartbeatPublicationGet
import no.nordicsemi.android.mesh.transport.ConfigHeartbeatPublicationSet
import no.nordicsemi.android.mesh.transport.ConfigHeartbeatSubscriptionGet
import no.nordicsemi.android.mesh.transport.ConfigHeartbeatSubscriptionSet
import no.nordicsemi.android.mesh.transport.ConfigModelAppBind
import no.nordicsemi.android.mesh.transport.ConfigModelAppUnbind
import no.nordicsemi.android.mesh.transport.ConfigNetKeyAdd
import no.nordicsemi.android.mesh.transport.ConfigNetKeyDelete
import no.nordicsemi.android.mesh.transport.ConfigNetKeyGet
import no.nordicsemi.android.mesh.transport.ConfigNetworkTransmitGet
import no.nordicsemi.android.mesh.transport.ConfigNetworkTransmitSet
import no.nordicsemi.android.mesh.transport.ConfigSigModelAppGet
import no.nordicsemi.android.mesh.transport.ConfigVendorModelAppGet
import no.nordicsemi.android.mesh.transport.GenericOnOffGet
import no.nordicsemi.android.mesh.transport.GenericOnOffSet
import java.util.UUID
import kotlin.random.Random

@CapacitorPlugin(
    name = "NrfMesh", permissions = [Permission(
        strings = [
            "android.permission.BLUETOOTH",
            "android.permission.BLUETOOTH_ADMIN",
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION",
        ], alias = "ANDROID_R"
    ), Permission(
        strings = [
            "android.permission.BLUETOOTH_SCAN",
            "android.permission.BLUETOOTH_CONNECT",
        ], alias = "ANDROID_S"
    )]
)
class NrfMeshPlugin : Plugin {

    private var alias: String = "ANDROID_R"

    private val async: AsyncManager = AsyncManager()
    private val mesh: MeshManager = MeshManager(async, this)
    private val ble: BleManager = BleManager(async, this, mesh)
    private val defaultTimeout = 20

    constructor() {
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.R) {
            this.alias = "ANDROID_S"
        }
    }

    override fun load() {
        super.load()
        this.ble.load(activity, context)
        this.mesh.load(activity, context)
    }

    @PermissionCallback
    @PluginMethod
    override fun checkPermissions(call: PluginCall) {
        if (!ble.assertFeature(call)) return
        if (!ble.assertAdapter(call)) return

        val permissions = super.handle.pluginAnnotation.permissions

        call.resolve(JSObject().apply {
            permissions.forEach { permission ->
                if (permission.alias == alias) {
                    permission.strings.forEach { key ->
                        val status = ActivityCompat.checkSelfPermission(context, key)
                        val state = if (status == PackageManager.PERMISSION_DENIED) {
                            PermissionState.DENIED
                        } else {
                            PermissionState.GRANTED
                        }
                        put(key, state)
                    }
                }
            }
        })
    }

    @PluginMethod
    override fun requestPermissions(call: PluginCall) {
        super.requestPermissionForAlias(this.alias, call, "checkPermissions")
    }

    public override fun notifyListeners(eventName: String?, data: JSObject?) {
        super.notifyListeners(eventName, data)
    }

    override fun handleOnStart() {
        super.handleOnStart()
        this.ble.handleOnStart(context)
    }

    override fun handleOnStop() {
        super.handleOnStop()
        this.ble.handleOnStop(context)
    }

    override fun handleOnDestroy() {
        super.handleOnDestroy()
        this.ble.handleOnStop(context)
    }

    @PluginMethod
    fun isBluetoothEnabled(call: PluginCall) {
        call.resolve(JSObject().apply {
            put("enabled", ble.isEnabled())
        })
    }

    @PluginMethod
    fun isConnected(call: PluginCall) {
        if (!this.ble.assertFeature(call)) return
        if (!this.ble.assertAdapter(call)) return
        if (!this.ble.assertEnabled(call)) return
        if (!this.mesh.assertNetwork(call)) return

        call.resolve(JSObject().apply {
            put("connected", ble.isConnected.value)
        })
    }

    @PluginMethod()
    fun connect(call: PluginCall) {
        if (!this.ble.assertFeature(call)) return
        if (!this.ble.assertAdapter(call)) return
        if (!this.ble.assertEnabled(call)) return
        if (!this.mesh.assertNetwork(call)) return

        CoroutineScope(Dispatchers.Default).launch {
            ble.connectToProvisioned()
            call.resolve()
        }
    }

    @PluginMethod
    fun disconnect(call: PluginCall) {
        if (!this.ble.assertFeature(call)) return
        if (!this.ble.assertAdapter(call)) return
        if (!this.ble.assertEnabled(call)) return
        if (!this.mesh.assertNetwork(call)) return

        CoroutineScope(Dispatchers.Default).launch {
            val timeout = call.getInt("timeout", defaultTimeout)
            async.on(call, MESH_STATE_DISCONNECTED).timeout(timeout!!)
            ble.disconnect()
        }
    }

    @PluginMethod
    fun export(call: PluginCall) {
        if (!this.ble.assertFeature(call)) return
        if (!this.ble.assertAdapter(call)) return
        if (!this.ble.assertEnabled(call)) return
        if (!this.mesh.assertNetwork(call)) return

        call.resolve(JSObject(mesh.export()))
    }

    @PluginMethod
    fun import(call: PluginCall) {
        if (!this.ble.assertFeature(call)) return
        if (!this.ble.assertAdapter(call)) return
        if (!this.ble.assertEnabled(call)) return
        if (!this.mesh.assertNetwork(call)) return

        try {
            mesh.import(call.data.toString())
            call.resolve()
        } catch (ex: Exception) {
            call.reject(ex.toString())
        }

    }

    @PluginMethod()
    fun netkeys(call: PluginCall) {
        if (!this.ble.assertFeature(call)) return
        if (!this.ble.assertAdapter(call)) return
        if (!this.ble.assertEnabled(call)) return
        if (!this.mesh.assertNetwork(call)) return

        try {
            call.resolve(JSObject().apply {
                put("netkeys", JSArray().apply {
                    mesh.netkeys.forEach { netkey ->
                        put(mesh.formatter.toJSON(netkey))
                    }
                })
            })
        } catch (ex: Exception) {
            call.reject(ex.message)
        }
    }

    @PluginMethod()
    fun netkey(call: PluginCall) {
        if (!this.ble.assertFeature(call)) return
        if (!this.ble.assertAdapter(call)) return
        if (!this.ble.assertEnabled(call)) return
        if (!this.mesh.assertNetwork(call)) return

        try {
            var netkey: NetworkKey? = null
            val addOpts = call.getObject("add")
            val delOpts = call.getObject("del")
            val getOpts = call.getObject("get")

            if (delOpts != null) {
                val index = delOpts.getInteger("index")
                    ?: return call.reject("index is required")

                netkey = mesh.netkeyDel(index)
            }

            if (addOpts != null) {
                netkey = mesh.netkeyAdd()
            }

            if (getOpts != null) {
                val index = getOpts.getInteger("index")
                    ?: return call.reject("index is required")

                netkey = mesh.netkeyGet(index)
            }

            if (netkey == null) {
                return call.reject("Failed to find netkey")
            }

            call.resolve(mesh.formatter.toJSON(netkey))
        } catch (ex: Exception) {
            call.reject(ex.message)
        }
    }

    @PluginMethod()
    fun appkeys(call: PluginCall) {
        if (!this.ble.assertFeature(call)) return
        if (!this.ble.assertAdapter(call)) return
        if (!this.ble.assertEnabled(call)) return
        if (!this.mesh.assertNetwork(call)) return

        try {
            call.resolve(JSObject().apply {
                put("appkeys", JSArray().apply {
                    val boundNetKey = call.getInt("boundNetKey")
                    val appkeys = if (boundNetKey == null)
                        mesh.appkeys else mesh.appkeys.filter { it.boundNetKeyIndex == boundNetKey }

                    appkeys.forEach { appkey ->
                        put(mesh.formatter.toJSON(appkey))
                    }
                })
            })
        } catch (ex: Exception) {
            call.reject(ex.message)
        }
    }

    @PluginMethod
    fun appkey(call: PluginCall) {
        if (!this.ble.assertFeature(call)) return
        if (!this.ble.assertAdapter(call)) return
        if (!this.ble.assertEnabled(call)) return
        if (!this.mesh.assertNetwork(call)) return

        try {
            var appkey: ApplicationKey? = null
            val addOpts = call.getObject("add")
            val delOpts = call.getObject("del")
            val getOpts = call.getObject("get")

            if (delOpts != null) {
                val index = delOpts.getInteger("index")
                    ?: return call.reject("index is required")

                appkey = mesh.appkeyDel(index)
            }

            if (addOpts != null) {
                appkey = mesh.appkeyAdd(addOpts.getInteger("boundNetKey"))
            }

            if (getOpts != null) {
                val index = getOpts.getInteger("index")
                    ?: return call.reject("index is required")

                appkey = mesh.appkeyGet(index)
            }

            if (appkey == null) {
                return call.reject("Failed to find appkey")
            }

            call.resolve(mesh.formatter.toJSON(appkey))
        } catch (ex: Exception) {
            call.reject(ex.message)
        }
    }

    @PluginMethod
    fun groups(call: PluginCall) {
        if (!this.ble.assertFeature(call)) return
        if (!this.ble.assertAdapter(call)) return
        if (!this.ble.assertEnabled(call)) return
        if (!this.mesh.assertNetwork(call)) return

        try {
            call.resolve(JSObject().apply {
                put("groups", JSArray().apply {
                    mesh.groups.forEach { group ->
                        put(mesh.formatter.toJSON(group))
                    }
                })
            })
        } catch (ex: Exception) {
            call.reject(ex.message)
        }
    }

    @PluginMethod
    fun group(call: PluginCall) {
        if (!this.ble.assertFeature(call)) return
        if (!this.ble.assertAdapter(call)) return
        if (!this.ble.assertEnabled(call)) return
        if (!this.mesh.assertNetwork(call)) return

        try {
            var group: Group? = null
            val addOpts = call.getObject("add")
            val delOpts = call.getObject("del")
            val getOpts = call.getObject("get")

            if (delOpts != null) {
                val address = delOpts.getInteger("address")
                    ?: return call.reject("address is required")

                group = mesh.groupDel(address)
            }

            if (addOpts != null) {
                val address = addOpts.getInteger("address")
                    ?: return call.reject("address is required")

                group = mesh.groupAdd(address)
            }

            if (getOpts != null) {
                val address = getOpts.getInteger("address")
                    ?: return call.reject("address is required")

                group = mesh.groupGet(address)
            }

            if (group == null) {
                return call.reject("Failed to find group")
            }

            call.resolve(mesh.formatter.toJSON(group))
        } catch (ex: Exception) {
            call.reject(ex.message)
        }
    }

    @PluginMethod
    fun nodes(call: PluginCall) {
        if (!this.ble.assertFeature(call)) return
        if (!this.ble.assertAdapter(call)) return
        if (!this.ble.assertEnabled(call)) return
        if (!this.mesh.assertNetwork(call)) return

        try {
            call.resolve(JSObject().apply {
                put("nodes", JSArray().apply {
                    mesh.nodes.forEach { node ->
                        put(mesh.formatter.toJSON(node))
                    }
                })
            })

        } catch (ex: Exception) {
            call.reject(ex.message)
        }
    }

    @PluginMethod
    fun init(call: PluginCall) {
        if (!this.ble.assertFeature(call)) return
        if (!this.ble.assertAdapter(call)) return
        if (!this.ble.assertEnabled(call)) return

        CoroutineScope(Dispatchers.Default).launch {
            try {
                async.on(call, MESH_NETWORK_INIT)
                ble.scan(true)
                mesh.init()
            } catch (ex: Exception) {
                call.reject(ex.message)
            }
        }
    }

    @PluginMethod
    fun kill(call: PluginCall) {
        if (!this.ble.assertFeature(call)) return
        if (!this.ble.assertAdapter(call)) return
        if (!this.ble.assertEnabled(call)) return

        CoroutineScope(Dispatchers.Default).launch {
            try {
                ble.disconnect()
                ble.scan(false)
                call.resolve()
            } catch (ex: Exception) {
                call.reject(ex.message)
            }
        }
    }

    @PluginMethod
    fun devices(call: PluginCall) {
        if (!this.ble.assertFeature(call)) return
        if (!this.ble.assertAdapter(call)) return
        if (!this.ble.assertEnabled(call)) return

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val timeout = call.getInt("timeout", 5000)
                val filter = call.getString("filter", "all")
                val devices = ble.devicesWithFilter(filter!!, timeout!!)

                call.resolve(JSObject().apply {
                    put("devices", JSArray().apply {
                        devices.forEach { device ->
                            put(mesh.formatter.toJSON(device))
                        }
                    })
                })
            } catch (ex: Exception) {
                call.reject(ex.message)
            }
        }
    }

    @PluginMethod
    fun identify(call: PluginCall) {
        if (!this.ble.assertFeature(call)) return
        if (!this.ble.assertAdapter(call)) return
        if (!this.ble.assertEnabled(call)) return
        if (!this.mesh.assertNetwork(call)) return

        CoroutineScope(Dispatchers.Default).launch {
            try {
                val address = call.getString("address")
                    ?: return@launch call.reject("address is required")
                val uuid = call.getString("uuid")
                    ?: return@launch call.reject("uuid is required")

                if (!ble.connectToUnprovisioned(address, uuid)) {
                    return@launch call.reject("Failed to connect to device : $address $uuid")
                }

                mesh.identify(UUID.fromString(uuid))

                val timeout = call.getInt("timeout", defaultTimeout)
                async.on(call, MESH_NODE_IDENTIFY).timeout(timeout!!)
            } catch (ex: Exception) {
                call.reject(ex.message)
            }
        }
    }

    @PluginMethod
    fun provision(call: PluginCall) {
        if (!this.ble.assertFeature(call)) return
        if (!this.ble.assertAdapter(call)) return
        if (!this.ble.assertEnabled(call)) return
        if (!this.mesh.assertNetwork(call)) return

        CoroutineScope(Dispatchers.Default).launch {
            try {
                val address = call.getString("address")
                    ?: return@launch call.reject("address is required")
                val uuid = call.getString("uuid")
                    ?: return@launch call.reject("uuid is required")

                if (!ble.connectToUnprovisioned(address, uuid)) {
                    return@launch call.reject("Failed to connect to device : $address $uuid")
                }

                mesh.provision(UUID.fromString(uuid))

                val timeout = call.getInt("timeout", defaultTimeout)
                async.on(call, MESH_NODE_PROVISION).timeout(timeout!!)
            } catch (ex: Exception) {
                call.reject(ex.message)
            }
        }
    }

    @PluginMethod
    fun composition(call: PluginCall) {
        if (!this.ble.assertFeature(call)) return
        if (!this.ble.assertAdapter(call)) return
        if (!this.ble.assertEnabled(call)) return
        if (!this.mesh.assertNetwork(call)) return

        CoroutineScope(Dispatchers.Default).launch {
            try {
                if (!ble.connectToProvisioned()) {
                    return@launch call.reject("Failed to connect to proxy")
                }

                val timeout = call.getInt("timeout", defaultTimeout)

                val unicastAddress = call.getInt("unicastAddress")
                    ?: return@launch call.reject("unicastAddress is required")

                val pairs = mutableListOf<MessagePair>()
                val message = ConfigCompositionDataGet()
                pairs.add(MessagePair(CONFIG_COMPOSITION_DATA_GET, message))

                async.on(call, pairs, unicastAddress).single().run(mesh).timeout(timeout!!)
            } catch (ex: Exception) {
                call.reject(ex.message)
            }
        }
    }

    @PluginMethod
    fun node(call: PluginCall) {
        if (!this.ble.assertFeature(call)) return
        if (!this.ble.assertAdapter(call)) return
        if (!this.ble.assertEnabled(call)) return
        if (!this.mesh.assertNetwork(call)) return

        CoroutineScope(Dispatchers.Default).launch {
            try {
                if (!ble.connectToProvisioned()) {
                    return@launch call.reject("Failed to connect to proxy")
                }

                val timeout = call.getInt("timeout", defaultTimeout)

                val unicastAddress = call.getInt("unicastAddress")
                    ?: return@launch call.reject("get options unicastAddress is required")

                val pairs = mutableListOf<MessagePair>()

                val defaultTtlOpts = call.getObject("defaultTTL")
                if (defaultTtlOpts != null) {
                    val setOpts = defaultTtlOpts.getJSObject("set")
                    if (setOpts != null) {
                        val ttl = setOpts.getInteger("ttl")
                            ?: return@launch call.reject("defaultTTL options ttl is required")

                        val message = ConfigDefaultTtlSet(ttl)
                        pairs.add(MessagePair(CONFIG_DEFAULT_TTL_SET, message))
                    } else {
                        val message = ConfigDefaultTtlGet()
                        pairs.add(MessagePair(CONFIG_DEFAULT_TTL_SET, message))
                    }
                }

                val networkTransmitOpts = call.getObject("networkTransmit")
                if (networkTransmitOpts != null) {
                    val setOpts = networkTransmitOpts.getJSObject("set")
                    if (setOpts != null) {
                        val count = setOpts.getInteger("count")
                            ?: return@launch call.reject("networkTransmit options count is required")
                        val intervalSteps = setOpts.getInteger("intervalSteps")
                            ?: return@launch call.reject("networkTransmit options intervalSteps is required")

                        val message = ConfigNetworkTransmitSet(count, intervalSteps)
                        pairs.add(MessagePair(CONFIG_NETWORK_TRANSMIT_SET, message))
                    } else {
                        val message = ConfigNetworkTransmitGet()
                        pairs.add(MessagePair(CONFIG_NETWORK_TRANSMIT_GET, message))
                    }
                }

                val netkeyOpts = call.getObject("netkey")
                if (netkeyOpts != null) {
                    val delOpts = netkeyOpts.getJSObject("del")
                    val addOpts = netkeyOpts.getJSObject("add")
                    val getOpts = netkeyOpts.getJSObject("get")

                    if (delOpts != null) {
                        val netkeyIndex = delOpts.getInteger("netkeyIndex")
                            ?: return@launch call.reject("netkey.del options netkeyIndex is required")

                        val netkey = mesh.netkeyGet(netkeyIndex)
                            ?: return@launch call.reject("Failed to find netkey")

                        val message = ConfigNetKeyDelete(netkey)
                        pairs.add(MessagePair(CONFIG_NETKEY_DELETE, message))
                    }

                    if (addOpts != null) {
                        val netkeyIndex = addOpts.getInteger("netkeyIndex")
                            ?: return@launch call.reject("netkey.add options netkeyIndex is required")

                        val netkey = mesh.netkeyGet(netkeyIndex)
                            ?: return@launch call.reject("Failed to find netkey")

                        val message = ConfigNetKeyAdd(netkey)
                        pairs.add(MessagePair(CONFIG_NETKEY_ADD, message))
                    }

                    if (getOpts != null) {
                        val message = ConfigNetKeyGet()
                        pairs.add(MessagePair(CONFIG_NETKEY_GET, message))
                    }
                }

                val appkeyOpts = call.getObject("appkey")
                if (appkeyOpts != null) {
                    val delOpts = appkeyOpts.getJSObject("del")
                    val addOpts = appkeyOpts.getJSObject("add")
                    val getOpts = appkeyOpts.getJSObject("get")

                    if (delOpts != null) {
                        val appkeyIndex = delOpts.getInteger("appkeyIndex")
                            ?: return@launch call.reject("appkey.del options appkeyIndex is required")

                        val appkey = mesh.appkeyGet(appkeyIndex)
                            ?: return@launch call.reject("Failed to find appkey")

                        val netkey = mesh.netkeyGet(appkey.boundNetKeyIndex)!!

                        val message = ConfigAppKeyDelete(netkey, appkey)
                        pairs.add(MessagePair(CONFIG_APPKEY_DELETE, message))
                    }

                    if (addOpts != null) {
                        val appkeyIndex = addOpts.getInteger("appkeyIndex")
                            ?: return@launch call.reject("appkey.del options appkeyIndex is required")

                        val appkey = mesh.appkeyGet(appkeyIndex)
                            ?: return@launch call.reject("Failed to find appkey")

                        val netkey = mesh.netkeyGet(appkey.boundNetKeyIndex)!!

                        val message = ConfigAppKeyAdd(netkey, appkey)
                        pairs.add(MessagePair(CONFIG_APPKEY_ADD, message))
                    }

                    if (getOpts != null) {
                        val netkeyIndex = getOpts.getInteger("netkeyIndex")
                        val netkey = if (netkeyIndex == null)
                            mesh.netkeyGet(0) else mesh.netkeyGet(netkeyIndex)

                        if (netkey == null) {
                            return@launch call.reject("Failed to find netkey")
                        }

                        val message = ConfigAppKeyGet(netkey)
                        pairs.add(MessagePair(CONFIG_APPKEY_GET, message))
                    }
                }

                val heartbeatOpts = call.getObject("heartbeat")
                if (heartbeatOpts != null) {
                    val heartbeatPubOpts = heartbeatOpts.getJSObject("pub")
                    val heartbeatSubOpts = heartbeatOpts.getJSObject("sub")

                    if (heartbeatPubOpts != null) {
                        val setOpts = heartbeatPubOpts.getJSObject("set")
                        if (setOpts != null) {
                            val firstNode = mesh.nodeGet(0x0001)!!

                            val period = setOpts.getInteger("period")
                                ?: return@launch call.reject("heartbeat.pub options period is required")
                            val ttl = setOpts.getInteger("ttl")
                                ?: return@launch call.reject("heartbeat.pub options ttl is required")

                            val count = setOpts.getInteger("count", 0xFFFF)!!
                            val address = setOpts.getInteger("address") ?: firstNode.unicastAddress
                            val netkeyIndex = setOpts.getInteger("netkeyIndex") ?: 0

                            val features = Features()
                            features.proxy = Features.ENABLED
                            features.relay = Features.ENABLED

                            val message = ConfigHeartbeatPublicationSet(
                                address,
                                Utils.encodeHeartbeatCount(count).toByte(),
                                Utils.encodeHeartbeatPeriod(period).toByte(),
                                ttl,
                                features,
                                netkeyIndex
                            )
                            pairs.add(MessagePair(CONFIG_HEARTBEAT_PUBLICATION_SET, message))
                        } else {
                            val message = ConfigHeartbeatPublicationGet()
                            pairs.add(MessagePair(CONFIG_HEARTBEAT_PUBLICATION_GET, message))
                        }
                    }

                    if (heartbeatSubOpts != null) {
                        val setOpts = heartbeatSubOpts.getJSObject("set")
                        if (setOpts != null) {
                            val srcAddress = setOpts.getInteger("srcAddress")
                                ?: return@launch call.reject("heartbeat.sub options srcAddress is required")
                            val dstAddress = setOpts.getInteger("dstAddress")
                                ?: return@launch call.reject("heartbeat.sub options dstAddress is required")
                            val period = setOpts.getInteger("period")
                                ?: return@launch call.reject("heartbeat.sub options period is required")

                            val message = ConfigHeartbeatSubscriptionSet(
                                srcAddress,
                                dstAddress,
                                Utils.encodeHeartbeatPeriod(period).toByte()
                            )

                            pairs.add(MessagePair(CONFIG_HEARTBEAT_SUBSCRIPTION_SET, message))
                        } else {
                            val message = ConfigHeartbeatSubscriptionGet()
                            pairs.add(MessagePair(CONFIG_HEARTBEAT_SUBSCRIPTION_GET, message))
                        }
                    }
                }

                val bindOpts = call.getObject("bind")
                if (bindOpts != null) {
                    val delOpts = bindOpts.getJSObject("del")
                    val addOpts = bindOpts.getJSObject("add")
                    val getOpts = bindOpts.getJSObject("get")

                    if (delOpts != null) {
                        val elementAddress = delOpts.getInteger("elementAddress")
                            ?: return@launch call.reject("bind.del options elementAddress is required")
                        val modelId = delOpts.getInteger("modelId")
                            ?: return@launch call.reject("bind.del options modelId is required")
                        val appkeyIndex = delOpts.getInteger("appkeyIndex")
                            ?: return@launch call.reject("bind.del options appkeyIndex is required")

                        val message = ConfigModelAppUnbind(elementAddress, modelId, appkeyIndex)
                        pairs.add(MessagePair(CONFIG_MODEL_APP_UNBIND, message))
                    }

                    if (addOpts != null) {
                        val elementAddress = addOpts.getInteger("elementAddress")
                            ?: return@launch call.reject("bind.add options elementAddress is required")
                        val modelId = addOpts.getInteger("modelId")
                            ?: return@launch call.reject("bind.add options modelId is required")
                        val appkeyIndex = addOpts.getInteger("appkeyIndex")
                            ?: return@launch call.reject("bind.add options appkeyIndex is required")

                        val message = ConfigModelAppBind(elementAddress, modelId, appkeyIndex)
                        pairs.add(MessagePair(CONFIG_MODEL_APP_BIND, message))
                    }

                    if (getOpts != null) {
                        val elementAddress = getOpts.getInteger("elementAddress")
                            ?: return@launch call.reject("bind.get options elementAddress is required")
                        val modelId = getOpts.getInteger("modelId")
                            ?: return@launch call.reject("bind.get options modelId is required")

                        val node = mesh.nodeGet(unicastAddress)
                            ?: return@launch call.reject("Failed to find node")
                        val element = node.elements.values.firstOrNull { ele ->
                            ele.elementAddress == elementAddress
                        } ?: return@launch call.reject("Failed to find element")
                        val model = element.meshModels.values.firstOrNull { mdl ->
                            mdl.modelId == modelId
                        } ?: return@launch call.reject("Failed to find model")

                        if (model is SigModel) {
                            val message = ConfigSigModelAppGet(elementAddress, modelId)
                            pairs.add(MessagePair(CONFIG_SIG_MODEL_APP_GET, message))
                        } else {
                            val message = ConfigVendorModelAppGet(elementAddress, modelId)
                            pairs.add((MessagePair(CONFIG_VENDOR_MODEL_APP_GET, message)))
                        }
                    }
                }

                async.on(call, pairs, unicastAddress).finished { data ->
                    val getOpts = call.getObject("get")
                    if (getOpts != null) {
                        val node = mesh.nodeGet(unicastAddress)
                        if (node != null) {
                            data.put("get", mesh.formatter.toJSON(node))
                        } else {
                            data.put(
                                "get",
                                JSObject().put("message", "Failed to find node")
                            )
                        }
                    }
                }.run(mesh).timeout(timeout!!)
            } catch (ex: Exception) {
                call.reject(ex.message)
            }
        }
    }

    @PluginMethod
    fun model(call: PluginCall) {
        if (!this.ble.assertFeature(call)) return
        if (!this.ble.assertAdapter(call)) return
        if (!this.ble.assertEnabled(call)) return
        if (!this.mesh.assertNetwork(call)) return

        CoroutineScope(Dispatchers.Default).launch {
            try {
                if (!ble.connectToProvisioned()) {
                    return@launch call.reject("Failed to connect to proxy")
                }

                val timeout = call.getInt("timeout", defaultTimeout)

                val elementAddress = call.getInt("elementAddress")
                    ?: return@launch call.reject("options elementAddress is required")
                val appkeyIndex = call.getInt("appkeyIndex")
                    ?: return@launch call.reject("options appkeyIndex is required")
                val appkey = mesh.appkeyGet(appkeyIndex)
                    ?: return@launch call.reject("Failed to find appkey")

                val pairs = mutableListOf<MessagePair>()

                val onoffOpts = call.getObject("onoff")
                if (onoffOpts != null) {
                    val setOpts = onoffOpts.getJSObject("set")
                    if (setOpts != null) {
                        val state = setOpts.getBool("state")
                        val message =
                            GenericOnOffSet(
                                appkey,
                                state == true,
                                Random.nextInt(),
                                null,
                                null,
                                null
                            )
                        pairs.add(MessagePair(GENERIC_ON_OFF_SET, message))
                    } else {
                        val message = GenericOnOffGet(appkey)
                        pairs.add(MessagePair(GENERIC_ON_OFF_GET, message))
                    }
                }

                val sensorOpts = call.getObject("sensor")
                if (sensorOpts != null) {
                    val propertyId = sensorOpts.getInteger("propertyId")
                        ?: return@launch call.reject("sensor options propertyId is required")

                    val getOpts = sensorOpts.getJSObject("get")
                    if (getOpts != null) {
                        val message = SensorGetWrapper(appkey, propertyId.toShort())
                        pairs.add(MessagePair(SENSOR_GET, message))
                    }

                    val propertyOpts = sensorOpts.getJSObject("property")
                    if (propertyOpts != null) {
                        val setPropertyId = propertyOpts.getInteger("propertyId")
                            ?: return@launch call.reject("sensor.property options propertyId is required")
                        val setBytes = propertyOpts.optJSONArray("bytes")
                        if (setBytes != null) {

                            val setByteArray = (0 until setBytes.length())
                                .map { setBytes.getInt(it).toByte() }


                            val message = SensorSettingSetWrapper(
                                appkey,
                                propertyId.toShort(),
                                setPropertyId.toShort(),
                                setByteArray.toByteArray()
                            )

                            pairs.add(MessagePair(SENSOR_SETTING_SET, message))
                        } else {
                            val message = SensorSettingGetWrapper(
                                appkey,
                                propertyId.toShort(),
                                setPropertyId.toShort()
                            )
                            pairs.add(MessagePair(SENSOR_SETTING_GET, message))
                        }
                    }

                    val propertiesOpts = sensorOpts.getJSObject("properties")
                    if (propertiesOpts != null) {
                        val message = SensorSettingsGetWrapper(
                            appkey,
                            propertyId.toShort()
                        )

                        pairs.add(MessagePair(SENSOR_SETTINGS_GET, message))
                    }
                }

                async.on(call, pairs, elementAddress).run(mesh).timeout(timeout!!)
            } catch (ex: Exception) {
                call.reject(ex.message)
            }
        }
    }

}