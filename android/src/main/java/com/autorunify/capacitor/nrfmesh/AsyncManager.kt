package com.autorunify.capacitor.nrfmesh

import com.getcapacitor.JSObject
import com.getcapacitor.PluginCall
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import no.nordicsemi.android.mesh.opcodes.ApplicationMessageOpCodes.GENERIC_ON_OFF_GET
import no.nordicsemi.android.mesh.opcodes.ApplicationMessageOpCodes.GENERIC_ON_OFF_SET
import no.nordicsemi.android.mesh.opcodes.ApplicationMessageOpCodes.GENERIC_ON_OFF_STATUS
import no.nordicsemi.android.mesh.opcodes.ApplicationMessageOpCodes.SENSOR_GET
import no.nordicsemi.android.mesh.opcodes.ApplicationMessageOpCodes.SENSOR_SETTINGS_GET
import no.nordicsemi.android.mesh.opcodes.ApplicationMessageOpCodes.SENSOR_SETTINGS_STATUS
import no.nordicsemi.android.mesh.opcodes.ApplicationMessageOpCodes.SENSOR_SETTING_GET
import no.nordicsemi.android.mesh.opcodes.ApplicationMessageOpCodes.SENSOR_SETTING_SET
import no.nordicsemi.android.mesh.opcodes.ApplicationMessageOpCodes.SENSOR_SETTING_STATUS
import no.nordicsemi.android.mesh.opcodes.ApplicationMessageOpCodes.SENSOR_STATUS
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_APPKEY_ADD
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_APPKEY_DELETE
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_APPKEY_GET
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_APPKEY_LIST
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_APPKEY_STATUS
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_COMPOSITION_DATA_GET
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_COMPOSITION_DATA_STATUS
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_DEFAULT_TTL_GET
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_DEFAULT_TTL_SET
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_DEFAULT_TTL_STATUS
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_HEARTBEAT_PUBLICATION_GET
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_HEARTBEAT_PUBLICATION_SET
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_HEARTBEAT_PUBLICATION_STATUS
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_HEARTBEAT_SUBSCRIPTION_GET
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_HEARTBEAT_SUBSCRIPTION_SET
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_HEARTBEAT_SUBSCRIPTION_STATUS
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_MODEL_APP_BIND
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_MODEL_APP_STATUS
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_MODEL_APP_UNBIND
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_NETKEY_ADD
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_NETKEY_DELETE
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_NETKEY_GET
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_NETKEY_LIST
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_NETKEY_STATUS
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_NETWORK_TRANSMIT_GET
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_NETWORK_TRANSMIT_SET
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_NETWORK_TRANSMIT_STATUS
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_SIG_MODEL_APP_GET
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_SIG_MODEL_APP_LIST
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_VENDOR_MODEL_APP_GET
import no.nordicsemi.android.mesh.opcodes.ConfigMessageOpCodes.CONFIG_VENDOR_MODEL_APP_LIST
import no.nordicsemi.android.mesh.transport.MeshMessage

abstract class AsyncCall {
    protected val call: PluginCall
    var done: Boolean
        protected set

    constructor(call: PluginCall) {
        this.done = false
        this.call = call
    }

    abstract fun match(code: Int, address: Int): AsyncCall?

    open fun trigger(data: JSObject?, message: String?) {
        if (data != null) this.resolve(data)
        if (message != null) this.reject(message)
    }

    open fun timeout(seconds: Int) {
        CoroutineScope(Dispatchers.Default).launch {
            val milliseconds = (seconds * 1000)
            delay(milliseconds.toLong())
            if (!done) {
                val reply = JSObject()
                reply.put("method", call.methodName)
                reply.put("data", JSObject().apply {
                    call.data.keys().forEach { key ->
                        put(key, call.data.get(key))
                    }
                })
                call.reject("Operation timed out", reply)
                done = true
            }
        }
    }

    private fun resolve(result: JSObject) {
        if (done) return

        call.resolve(result)
        done = true
    }

    private fun reject(message: String) {
        if (done) return

        call.reject(message)
        done = true
    }

    protected fun opPair(code: Int): Int {
        return when (code) {
            CONFIG_COMPOSITION_DATA_GET -> CONFIG_COMPOSITION_DATA_STATUS
            CONFIG_DEFAULT_TTL_GET, CONFIG_DEFAULT_TTL_SET -> CONFIG_DEFAULT_TTL_STATUS
            CONFIG_NETWORK_TRANSMIT_GET, CONFIG_NETWORK_TRANSMIT_SET -> CONFIG_NETWORK_TRANSMIT_STATUS
            CONFIG_NETKEY_ADD, CONFIG_NETKEY_DELETE -> CONFIG_NETKEY_STATUS
            CONFIG_NETKEY_GET -> CONFIG_NETKEY_LIST
            CONFIG_APPKEY_ADD, CONFIG_APPKEY_DELETE -> CONFIG_APPKEY_STATUS
            CONFIG_APPKEY_GET -> CONFIG_APPKEY_LIST
            CONFIG_HEARTBEAT_PUBLICATION_GET, CONFIG_HEARTBEAT_PUBLICATION_SET -> CONFIG_HEARTBEAT_PUBLICATION_STATUS
            CONFIG_HEARTBEAT_SUBSCRIPTION_GET, CONFIG_HEARTBEAT_SUBSCRIPTION_SET -> CONFIG_HEARTBEAT_SUBSCRIPTION_STATUS
            CONFIG_MODEL_APP_BIND, CONFIG_MODEL_APP_UNBIND -> CONFIG_MODEL_APP_STATUS
            CONFIG_SIG_MODEL_APP_GET -> CONFIG_SIG_MODEL_APP_LIST
            CONFIG_VENDOR_MODEL_APP_GET -> CONFIG_VENDOR_MODEL_APP_LIST
            GENERIC_ON_OFF_GET, GENERIC_ON_OFF_SET -> GENERIC_ON_OFF_STATUS
            SENSOR_GET -> SENSOR_STATUS
            SENSOR_SETTING_SET, SENSOR_SETTING_GET -> SENSOR_SETTING_STATUS
            SENSOR_SETTINGS_GET -> SENSOR_SETTINGS_STATUS


//            CONFIG_NODE_RESET -> CONFIG_NODE_RESET_STATUS
//            CONFIG_MODEL_SUBSCRIPTION_ADD, CONFIG_MODEL_SUBSCRIPTION_DELETE, CONFIG_MODEL_SUBSCRIPTION_DELETE_ALL -> CONFIG_MODEL_SUBSCRIPTION_STATUS
//            CONFIG_MODEL_PUBLICATION_SET, CONFIG_MODEL_PUBLICATION_GET -> CONFIG_MODEL_PUBLICATION_STATUS
//            SENSOR_DESCRIPTOR_GET -> SENSOR_DESCRIPTOR_STATUS
//            SENSOR_COLUMN_GET -> SENSOR_COLUMN_STATUS
//            SENSOR_SERIES_GET -> SENSOR_SERIES_STATUS
//            SENSOR_CADENCE_GET -> SENSOR_CADENCE_STATUS
//            GENERIC_LEVEL_GET, GENERIC_LEVEL_SET, GENERIC_LEVEL_SET_UNACKNOWLEDGED -> GENERIC_POWER_LEVEL_STATUS
//            GENERIC_POWER_LEVEL_GET, GENERIC_POWER_LEVEL_SET, GENERIC_POWER_LEVEL_SET_UNACKNOWLEDGED -> GENERIC_POWER_LEVEL_STATUS
//            LIGHT_HSL_GET, LIGHT_HSL_SET, LIGHT_HSL_SET_UNACKNOWLEDGED -> LIGHT_HSL_STATUS
//            LIGHT_CTL_GET, LIGHT_CTL_SET, LIGHT_CTL_SET_UNACKNOWLEDGED -> LIGHT_CTL_STATUS

            else -> code
        }
    }

    protected fun opKey(code: Int): String {
        return when (code) {
            CONFIG_DEFAULT_TTL_GET, CONFIG_DEFAULT_TTL_SET -> "defaultTTL"
            CONFIG_NETWORK_TRANSMIT_GET, CONFIG_NETWORK_TRANSMIT_SET -> "networkTransmit"
            CONFIG_NETKEY_ADD -> "netkey.add"
            CONFIG_NETKEY_DELETE -> "netkey.del"
            CONFIG_NETKEY_GET -> "netkey.get"
            CONFIG_APPKEY_ADD -> "appkey.add"
            CONFIG_APPKEY_DELETE -> "appkey.del"
            CONFIG_APPKEY_GET -> "appkey.get"
            CONFIG_HEARTBEAT_PUBLICATION_GET, CONFIG_HEARTBEAT_PUBLICATION_SET -> "heartbeat.pub"
            CONFIG_HEARTBEAT_SUBSCRIPTION_GET, CONFIG_HEARTBEAT_SUBSCRIPTION_SET -> "heartbeat.sub"
            CONFIG_MODEL_APP_BIND -> "bind.add"
            CONFIG_MODEL_APP_UNBIND -> "bind.del"
            CONFIG_SIG_MODEL_APP_GET, CONFIG_VENDOR_MODEL_APP_GET -> "bind.get"
            GENERIC_ON_OFF_GET, GENERIC_ON_OFF_SET -> "onoff"
            SENSOR_GET -> "sensor.get"
            SENSOR_SETTING_SET, SENSOR_SETTING_GET -> "sensor.property"
            SENSOR_SETTINGS_GET -> "sensor.properties"

            else -> "unknown"
        }
    }
}

class SystemAsyncCall : AsyncCall {
    companion object {
        const val MESH_NETWORK_INIT = 0x08000000;
        const val MESH_NODE_IDENTIFY = 0x08000001;
        const val MESH_NODE_PROVISION = 0x08000002;
        const val MESH_STATE_CONNECTED = 0x08000003;
        const val MESH_STATE_DISCONNECTED = 0x08000004;
    }

    private val code: Int
    private val address: Int

    constructor(call: PluginCall, code: Int, address: Int) : super(call) {
        this.code = opPair(code)
        this.address = address
    }

    override fun match(code: Int, address: Int): AsyncCall? {
        if (this.code == code && this.address == address) return this

        return null
    }
}

class MessagePair {
    val msg: MeshMessage
    val originCode: Int
    var address: Int
    var code: Int


    constructor(code: Int, msg: MeshMessage) {
        this.originCode = code
        this.code = code
        this.msg = msg
        this.address = 0
    }
}

class MessageAsyncCall : AsyncCall {
    private val result: JSObject
    private var mesh: MeshManager? = null
    private val address: Int
    private val pairs: MutableList<MessagePair>
    private var isSingle: Boolean
    private var finishedCall: ((JSObject) -> Unit)? = null

    constructor(call: PluginCall, pairs: MutableList<MessagePair>, address: Int) : super(call) {
        this.address = address
        this.pairs = pairs
        this.result = JSObject()
        this.isSingle = false

        this.pairs.forEach { pair ->
            pair.code = opPair(pair.code)
            pair.address = address
        }
    }

    override fun match(code: Int, address: Int): AsyncCall? {
        if (this.address != address) return null
        val pair = pairs.firstOrNull()

        if (pair != null) {
            return if (pair.code == code) this
            else null
        }

        done = true
        return null
    }

    override fun trigger(data: JSObject?, message: String?) {
        if (message != null) {
            return super.trigger(null, message)
        }

        val pair = pairs.firstOrNull()
        if (data != null && pair != null) {
            if (isSingle) {
                finishedCall?.invoke(data)
                return super.trigger(data, null)
            }


            val jsonKeys = opKey(pair.originCode).split(".")
            val jsonKey = jsonKeys.last()
            var parent = result

            jsonKeys.forEach { key ->
                if (key != jsonKey) {
                    if (!parent.has(key)) {
                        parent.put(key, JSObject())
                    }

                    parent = parent.get(key) as JSObject
                } else {
                    parent.put(key, data)
                }
            }
        }

        pairs.removeFirstOrNull()
        if (pairs.isEmpty()) {
            finishedCall?.invoke(result)
            return super.trigger(result, null)
        }

        this.run(mesh!!)
    }

    override fun timeout(seconds: Int) {
        if (done) return
        super.timeout(seconds * this.pairs.count())
    }

    fun run(mesh: MeshManager): MessageAsyncCall {
        this.mesh = mesh

        val pair = pairs.firstOrNull()
        if (pair == null) {
            super.trigger(result, null)
            return this
        }

        mesh.createMeshPdu(pair.address, pair.msg)
        return this
    }

    fun finished(action: (JSObject) -> Unit): MessageAsyncCall {
        this.finishedCall = action
        return this
    }

    fun single(): MessageAsyncCall {
        this.isSingle = true
        return this
    }
}

class AsyncManager {
    private val calls: MutableList<AsyncCall> = mutableListOf<AsyncCall>()

    private fun removeDoneCalls() {
        val doneCalls = this.calls.filter {
            it.done
        }

        doneCalls.forEach {
            calls.remove(it)
        }
    }


    fun on(call: PluginCall, code: Int, address: Int = 0): AsyncCall {
        removeDoneCalls()
        val async = SystemAsyncCall(call, code, address)
        calls.add(async)
        return async
    }

    fun on(call: PluginCall, pairs: MutableList<MessagePair>, address: Int): MessageAsyncCall {
        removeDoneCalls()
        val async = MessageAsyncCall(call, pairs, address)
        calls.add(async)
        return async
    }


    fun emit(data: JSObject, code: Int, address: Int = 0) {
        removeDoneCalls()
        calls.forEach { call ->
            call.match(code, address)?.trigger(data, null)
        }
        removeDoneCalls()
    }

    fun error(message: String, code: Int, address: Int) {
        removeDoneCalls()
        calls.forEach { call ->
            call.match(code, address)?.trigger(null, message)
        }
        removeDoneCalls()
    }
}