package com.autorunify.capacitor.nrfmesh

import no.nordicsemi.android.mesh.ApplicationKey
import no.nordicsemi.android.mesh.sensorutils.Bool
import no.nordicsemi.android.mesh.sensorutils.DeviceProperty
import no.nordicsemi.android.mesh.transport.SensorGet
import no.nordicsemi.android.mesh.transport.SensorSettingGet
import no.nordicsemi.android.mesh.transport.SensorSettingSet
import no.nordicsemi.android.mesh.transport.SensorSettingStatus
import no.nordicsemi.android.mesh.transport.SensorSettingsGet
import no.nordicsemi.android.mesh.transport.SensorSettingsStatus
import no.nordicsemi.android.mesh.transport.SensorStatus
import no.nordicsemi.android.mesh.utils.MeshParserUtils
import no.nordicsemi.android.mesh.utils.SensorFormat
import java.nio.ByteBuffer
import java.nio.ByteOrder.LITTLE_ENDIAN

class SensorGetWrapper : SensorGet {
    constructor(appkey: ApplicationKey, propertyId: Short)
            : super(appkey, DeviceProperty.UNKNOWN) {
        this.mParameters = ByteBuffer
            .allocate(2)
            .order(LITTLE_ENDIAN)
            .putShort(propertyId)
            .array()
    }
}

class SensorSettingSetWrapper : SensorSettingSet {
    constructor(
        appkey: ApplicationKey,
        propertyId: Short,
        sensorSettingPropertyId: Short,
        sensorSetting: ByteArray
    ) : super(
        appkey,
        DeviceProperty.UNKNOWN,
        DeviceProperty.UNKNOWN,
        Bool(false)
    ) {
        this.mParameters = ByteBuffer
            .allocate(4 + sensorSetting.size)
            .order(LITTLE_ENDIAN)
            .putShort(propertyId)
            .putShort(sensorSettingPropertyId)
            .put(sensorSetting)
            .array()
    }
}

class SensorSettingGetWrapper : SensorSettingGet {
    constructor(
        appkey: ApplicationKey,
        propertyId: Short,
        sensorSettingPropertyId: Short,
    ) : super(
        appkey,
        DeviceProperty.UNKNOWN,
        DeviceProperty.UNKNOWN
    ) {
        this.mParameters = ByteBuffer
            .allocate(4)
            .order(LITTLE_ENDIAN)
            .putShort(propertyId)
            .putShort(sensorSettingPropertyId)
            .array()
    }
}

class SensorSettingsGetWrapper : SensorSettingsGet {
    constructor(appkey: ApplicationKey, propertyId: Short) : super(
        appkey,
        DeviceProperty.UNKNOWN
    ) {
        this.mParameters = ByteBuffer
            .allocate(2)
            .order(LITTLE_ENDIAN)
            .putShort(propertyId)
            .array()
    }
}

class SensorSettingStatusWrapper {
    val propertyId: Short
    val sensorSettingPropertyId: Short
    val sensorSetting: ByteArray

    constructor(status: SensorSettingStatus) {
        val parameters = status.parameters

        this.propertyId = MeshParserUtils.unsignedBytesToInt(
            parameters[0],
            parameters[1]
        ).toShort()

        this.sensorSettingPropertyId = MeshParserUtils.unsignedBytesToInt(
            parameters[2],
            parameters[3]
        ).toShort()

        if (parameters.size <= 4) {
            this.sensorSetting = ByteArray(0)
        } else {
            this.sensorSetting = parameters.copyOfRange(5, parameters.size)
        }
    }
}

class SensorSettingsStatusWrapper {
    val sensorSettingPropertyIds: ArrayList<Short>

    constructor(status: SensorSettingsStatus) {
        this.sensorSettingPropertyIds = arrayListOf<Short>()

        val parameters = status.parameters
        var offset = 2
        while (offset < parameters.size) {
            val propertyId = MeshParserUtils.unsignedBytesToInt(
                parameters[offset],
                parameters[offset + 1]
            )
            this.sensorSettingPropertyIds.add(propertyId.toShort())
            offset += 2
        }
    }
}

class SensorValue {
    val propertyId: Short
    val bytes: ByteArray

    constructor(propertyId: Short, bytes: ByteArray) {
        this.propertyId = propertyId
        this.bytes = bytes
    }
}

class SensorStatusWrapper {
    val values: ArrayList<SensorValue>

    constructor(status: SensorStatus) {
        this.values = arrayListOf<SensorValue>()

        val parameters = status.parameters

        var offset = 0
        var sensorFormat: SensorFormat
        var length: Int
        var propertyId: Short
        while (offset < parameters.size) {
            val octet0 = parameters[offset++].toInt() and 0xFF
            val octet1 = parameters[offset++].toInt() and 0xFF
            sensorFormat = SensorFormat.from(((octet0) and 0x01).toByte())
            when (sensorFormat) {
                SensorFormat.FORMAT_A -> {
                    length = ((octet0 and 0x1E) shr 1) + 1 // zero based
                    propertyId = ((octet1 shl 3) or ((octet0) shr 5)).toShort()
                }

                SensorFormat.FORMAT_B -> {
                    val octet2 = parameters[offset++].toInt() and 0xFF
                    val tempLength = ((octet0 and 0xFE) shr 1)
                    length = if (tempLength == 0x7F) 0 else tempLength
                    propertyId = (octet2 or octet1).toShort()
                }
            }

            val bytes: ByteArray = parameters.copyOfRange(offset, offset + length)
            this.values.add(SensorValue(propertyId, bytes))
            offset += length
        }
    }
}