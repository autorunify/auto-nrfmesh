package com.autorunify.capacitor.nrfmesh

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import com.getcapacitor.JSArray
import com.getcapacitor.JSObject
import no.nordicsemi.android.mesh.ApplicationKey
import no.nordicsemi.android.mesh.Group
import no.nordicsemi.android.mesh.NetworkKey
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
import no.nordicsemi.android.mesh.transport.ConfigStatusMessage.StatusCodeNames
import no.nordicsemi.android.mesh.transport.ConfigVendorModelAppList
import no.nordicsemi.android.mesh.transport.GenericOnOffStatus
import no.nordicsemi.android.mesh.transport.ProvisionedMeshNode
import no.nordicsemi.android.mesh.transport.SensorSettingStatus
import no.nordicsemi.android.mesh.transport.SensorSettingsStatus
import no.nordicsemi.android.mesh.transport.SensorStatus
import no.nordicsemi.android.mesh.utils.CompanyIdentifiers
import no.nordicsemi.android.mesh.utils.CompositionDataParser
import no.nordicsemi.android.mesh.utils.MeshParserUtils
import org.json.JSONArray
import java.util.Locale

class FormatManager {
    private val dateFormat: SimpleDateFormat =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.US)

    fun toJSON(device: MeshDevice): JSObject {
        return JSObject().apply {
            put("address", device.address)
            put("name", device.name)
            put("rssi", device.rssi)
            put("provisioned", device.provisioned)

            if (!device.provisioned) {
                put("uuid", device.uuid)
            }
        }
    }

    fun toJSON(netkey: NetworkKey): JSObject {
        return JSObject().apply {
            put("index", netkey.keyIndex)
            put("key", MeshParserUtils.bytesToHex(netkey.key, false))
            put("minSecurity", if (netkey.isMinSecurity) "secure" else "insecure")
            put("name", netkey.name)
            put("phase", netkey.phase)
            put("timestamp", dateFormat.format(netkey.timestamp))
            if (netkey.oldKey != null) {
                put("oldKey", MeshParserUtils.bytesToHex(netkey.oldKey, false))
            }
        }
    }

    fun toJSON(appkey: ApplicationKey): JSObject {
        return JSObject().apply {
            put("index", appkey.keyIndex)
            put("boundNetKey", appkey.boundNetKeyIndex)
            put("key", MeshParserUtils.bytesToHex(appkey.key, false))
            put("name", appkey.name)
        }
    }

    fun toJSON(group: Group): JSObject {
        return JSObject().apply {
            put("name", group.name)
            put("address", group.address)
            put("addressHex", "0x" + group.address.toString(16))
        }
    }

    fun toJSON(unode: UnprovisionedMeshNode): JSObject {
        return JSObject().apply {
            put("numberOfElements", unode.provisioningCapabilities.numberOfElements)
            val oobTypeArray = JSArray().apply {
                unode.provisioningCapabilities.availableOOBTypes.forEach {
                    put(it)
                }
            }
            put("availableOOBTypes", oobTypeArray)
            put("algorithms", unode.provisioningCapabilities.rawAlgorithm)
            put("publicKeyType", unode.provisioningCapabilities.rawPublicKeyType)
            put("staticOobTypes", unode.provisioningCapabilities.rawStaticOOBType)
            put("outputOobSize", unode.provisioningCapabilities.outputOOBSize)
            put("outputOobActions", unode.provisioningCapabilities.rawOutputOOBAction)
            put("inputOobSize", unode.provisioningCapabilities.inputOOBSize)
            put("inputOobActions", unode.provisioningCapabilities.rawInputOOBAction)
        }
    }

    @SuppressLint("RestrictedApi")
    fun toJSON(node: ProvisionedMeshNode): JSObject {
        return JSObject().apply {
            put("name", node.nodeName)
            put("deviceKey", MeshParserUtils.bytesToHex(node.deviceKey, false))
            put("unicastAddress", node.unicastAddress)
            put("security", if (node.security == 1) "secure" else "insecure")
            put("ttl", node.ttl)
            put("netkeys", JSArray().apply {
                node.addedNetKeys.forEach { netkey ->
                    put(JSObject().apply {
                        put("index", netkey.index)
                        put("updated", netkey.isUpdated)
                    })
                }
            })
            put("appkeys", JSArray().apply {
                node.addedAppKeys.forEach {
                    put(JSObject().apply {
                        put("index", it.index)
                        put("updated", it.isUpdated)
                    })
                }
            })
            put("elements", JSArray().apply {
                node.elements.values.forEach {
                    put(JSObject().apply {
                        put("name", it.name)
                        put("elementAddress", it.elementAddress)
                        put("models", JSArray().apply {
                            it.meshModels.forEach { (_, model) ->
                                put(JSObject().apply {
                                    put("modelId", model.modelId)
                                    put("appkeys", JSArray().apply {
                                        model.boundAppKeyIndexes.forEach { appkey ->
                                            put(appkey)
                                        }
                                    })
                                    put("subscriptions", JSArray().apply {
                                        model.subscribedAddresses.forEach { address ->
                                            put(address)
                                        }
                                    })
                                    if (model.publicationSettings != null) {
                                        put("publication", model.publicationSettings.publishAddress)
                                    }
                                })
                            }
                        })
                    })
                }
            })
            if (node.nodeFeatures != null) {
                put("features", JSObject().apply {
                    put("friend", node.nodeFeatures.friend)
                    put("lowPower", node.nodeFeatures.lowPower)
                    put("proxy", node.nodeFeatures.proxy)
                    put("relay", node.nodeFeatures.relay)
                })
            }
            if (node.networkTransmitSettings != null) {
                put("networkTransmit", JSObject().apply {
                    put("count", node.networkTransmitSettings.networkTransmitCount)
                    put("interval", node.networkTransmitSettings.networkTransmissionInterval)
                    put("steps", node.networkTransmitSettings.networkIntervalSteps)
                })
            }
        }
    }

    fun toJSON(msg: ConfigCompositionDataStatus): JSObject {
        return JSObject().apply {
            put(
                "companyIdentifier",
                CompanyIdentifiers.getCompanyName(msg.companyIdentifier.toShort())
            )
            put(
                "productIdentifier", CompositionDataParser.formatProductIdentifier(
                    msg.productIdentifier, false
                )
            )
            put(
                "productVersion", CompositionDataParser.formatVersionIdentifier(
                    msg.versionIdentifier, false
                )
            )
            put("nodeFeaturesSupported", JSObject().apply {
                put("relay", msg.isRelayFeatureSupported)
                put("proxy", msg.isProxyFeatureSupported)
                put("friend", msg.isFriendFeatureSupported)
                put("lowPower", msg.isLowPowerFeatureSupported)
            })
            put("elements", JSArray().apply {
                msg.elements.values.forEach { elem ->
                    put(JSObject().apply {
                        put("name", elem.name)
                        put("elementAddress", elem.elementAddress)
                        put("sigModelCount", elem.sigModelCount)
                        put("vendorModelCount", elem.vendorModelCount)
                        put("location", elem.locationDescriptor)
                        put("models", JSArray().apply {
                            elem.meshModels.values.forEach { model ->
                                put(JSObject().apply {
                                    put("modelId", model.modelId)
                                    put("modelName", model.modelName)
                                    put("boundAppKeyIndexes", JSArray().apply {
                                        model.boundAppKeyIndexes.forEach {
                                            put(it)
                                        }
                                    })
                                    if (model.subscribedAddresses != null) {
                                        put("subscribedAddresses", JSArray().apply {
                                            model.subscribedAddresses.forEach {
                                                put(it)
                                            }
                                        })
                                    }

                                    if (model.publicationSettings != null) {
                                        put("publicationSettings", JSObject().apply {
                                            put(
                                                "appKeyIndex",
                                                model.publicationSettings.appKeyIndex
                                            )
                                            put(
                                                "publishAddress",
                                                model.publicationSettings.publishAddress
                                            )
                                            put(
                                                "credentialFlag",
                                                model.publicationSettings.credentialFlag
                                            )
                                            put(
                                                "publishTtl",
                                                model.publicationSettings.publishTtl
                                            )
                                            put(
                                                "publicationSteps",
                                                model.publicationSettings.publicationSteps
                                            )
                                            put(
                                                "publicationResolution",
                                                model.publicationSettings.publicationResolution
                                            )
                                            put(
                                                "retransmitCount",
                                                model.publicationSettings.publishRetransmitCount
                                            )
                                            put(
                                                "retransmitIntervalSteps",
                                                model.publicationSettings.publishRetransmitIntervalSteps
                                            )
                                        })
                                    }
                                })
                            }
                        })
                    })
                }
            })
        }
    }

    fun toJSON(msg: ConfigDefaultTtlStatus): JSObject {
        return JSObject().apply {
            put("ttl", msg.ttl)
        }
    }

    fun toJSON(msg: ConfigNetworkTransmitStatus): JSObject {
        return JSObject().apply {
            put("count", msg.networkTransmitCount)
            put("intervalSteps", msg.networkTransmitIntervalSteps)
        }
    }

    fun toJSON(msg: ConfigNetKeyStatus): JSObject {
        return JSObject().apply {
            put("netkeyIndex", msg.netKeyIndex)
        }
    }

    fun toJSON(msg: ConfigNetKeyList): JSObject {
        return JSObject().apply {
            put("netkeyIndexes", JSArray().apply {
                msg.keyIndexes.forEach { index ->
                    put(index)
                }
            })
        }
    }

    fun toJSON(msg: ConfigAppKeyStatus): JSObject {
        return JSObject().apply {
            put("netkeyIndex", msg.netKeyIndex)
            put("appkeyIndex", msg.appKeyIndex)
        }
    }

    fun toJSON(msg: ConfigAppKeyList): JSObject {
        return JSObject().apply {
            put("netkeyIndex", msg.netKeyIndex)
            put("appkeyIndexes", JSArray().apply {
                msg.keyIndexes.forEach { appkey ->
                    put(appkey)
                }
            })
        }
    }

    fun toJSON(msg: ConfigHeartbeatPublicationStatus): JSObject {
        val pub = msg.heartbeatPublication
        return JSObject().apply {
            put("address", pub.dst)
            put("count", Utils.decodeHeartbeatCount(pub.countLog.toInt()))
            put("period", Utils.decodeHeartbeatPeriod(pub.periodLog.toInt()))
            put("ttl", pub.ttl)
            put("netkeyIndex", pub.netKeyIndex)
            put("features", JSObject().apply {
                put("friend", pub.features.friend)
                put("proxy", pub.features.proxy)
                put("relay", pub.features.relay)
                put("lowPower", pub.features.lowPower)
            })
        }
    }

    fun toJSON(msg: ConfigHeartbeatSubscriptionStatus): JSObject {
        val sub = msg.heartbeatSubscription
        return JSObject().apply {
            put("srcAddress", sub.src)
            put("dstAddress", sub.dst)
            put("period", Utils.decodeHeartbeatPeriod(sub.periodLog.toInt()))
            put("count", Utils.decodeHeartbeatCount(sub.countLog.toInt()))
            put("minHops", sub.minHops)
            put("maxHops", sub.maxHops)
        }
    }

    fun toJSON(msg: ConfigModelAppStatus): JSObject {
        return JSObject().apply {
            put("modelId", msg.modelIdentifier)
            put("appkeyIndex", msg.appKeyIndex)
        }
    }

    fun toJSON(msg: ConfigSigModelAppList): JSObject {
        return JSObject().apply {
            put("modelId", msg.modelIdentifier)
            put("appkeyIndexs", JSONArray().apply {
                msg.keyIndexes.forEach { appkeyIndex ->
                    put(appkeyIndex)
                }
            })
        }
    }

    fun toJSON(msg: ConfigVendorModelAppList): JSObject {
        return JSObject().apply {
            put("modelId", msg.modelIdentifier)
            put("appkeyIndexs", JSONArray().apply {
                msg.keyIndexes.forEach { appkeyIndex ->
                    put(appkeyIndex)
                }
            })
        }
    }

    fun toJSON(msg: GenericOnOffStatus): JSObject {
        return JSObject().apply {
            put("state", msg.presentState)
        }
    }

    fun toJSON(msg: SensorStatus): JSObject {
        val m = SensorStatusWrapper(msg)
        return JSObject().apply {
            put("values", JSONArray().apply {
                m.values.forEach { sensorValue ->
                    put(JSObject().apply {
                        put("propertyId", sensorValue.propertyId)
                        put("bytes", JSONArray().apply {
                            sensorValue.bytes.forEach { byte ->
                                put(byte)
                            }
                        })
                    })
                }
            })
        }
    }

    fun toJSON(msg: SensorSettingStatus): JSObject {
        val m = SensorSettingStatusWrapper(msg)
        return JSObject().apply {
            put("propertyId", m.sensorSettingPropertyId)
            put("bytes", JSONArray().apply {
                m.sensorSetting.forEach { byte ->
                    put(byte)
                }
            })
        }
    }

    fun toJSON(msg: SensorSettingsStatus): JSObject {
        val m = SensorSettingsStatusWrapper(msg)
        return JSObject().apply {
            put("properties", JSONArray().apply {
                m.sensorSettingPropertyIds.forEach { property ->
                    put(JSObject().apply {
                        put("propertyId", property)
                    })
                }
            })
        }
    }

    fun toError(msg: ConfigStatusMessage): String {
        val statusName = when (StatusCodeNames.fromStatusCode(msg.statusCode)) {
            StatusCodeNames.SUCCESS -> "Success"
            StatusCodeNames.INVALID_ADDRESS -> "Invalid Address"
            StatusCodeNames.INVALID_MODEL -> "Invalid Model"
            StatusCodeNames.INVALID_APPKEY_INDEX -> "Invalid ApplicationKey Index"
            StatusCodeNames.INVALID_NETKEY_INDEX -> "Invalid NetKey Index"
            StatusCodeNames.INSUFFICIENT_RESOURCES -> "Insufficient Resources"
            StatusCodeNames.KEY_INDEX_ALREADY_STORED -> "Key Index Already Stored"
            StatusCodeNames.INVALID_PUBLISH_PARAMETERS -> "Invalid Publish Parameters"
            StatusCodeNames.NOT_A_SUBSCRIBE_MODEL -> "Not a Subscribe Model"
            StatusCodeNames.STORAGE_FAILURE -> "Storage Failure"
            StatusCodeNames.FEATURE_NOT_SUPPORTED -> "Feature Not Supported"
            StatusCodeNames.CANNOT_UPDATE -> "Cannot Update"
            StatusCodeNames.CANNOT_REMOVE -> "Cannot Remove"
            StatusCodeNames.CANNOT_BIND -> "Cannot Bind"
            StatusCodeNames.TEMPORARILY_UNABLE_TO_CHANGE_STATE -> "Temporarily Unable to Change State"
            StatusCodeNames.CANNOT_SET -> "Cannot Set"
            StatusCodeNames.UNSPECIFIED_ERROR -> "Unspecified Error"
            StatusCodeNames.INVALID_BINDING -> "Invalid Binding"
            StatusCodeNames.RFU -> "RFU"
            else -> "Unknown"
        }

        return "Message Error(${msg.statusCode}): $statusName"
    }
}