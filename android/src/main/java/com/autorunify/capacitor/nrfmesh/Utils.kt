package com.autorunify.capacitor.nrfmesh

import kotlin.math.ln
import kotlin.math.pow

class Utils {
    companion object {
        fun decodeHeartbeatPeriod(periodLog: Int): Int {
            if (periodLog == 0x00) return 0x0000
            if (periodLog == 0x11 || periodLog == -1) return 0xFFFF

            return (2.0).pow(periodLog.toDouble() - 1).toInt()
        }

        fun encodeHeartbeatPeriod(period: Int): Int {
            if (period == 0x0000) return 0x00
            if (period == 0xFFFF) return 0x11

            return ((ln(period.toDouble()) / ln(2.0)) + 1).toInt()
        }

        fun decodeHeartbeatCount(countLog: Int): Int {
            if (countLog == 0x00) return 0x0000
            if (countLog == -1 || countLog == 0x11) return 0xFFFF
            return ((2.0).pow(countLog.toDouble() - 1)).toInt()
        }

        fun encodeHeartbeatCount(count: Int): Int {
            if (count == 0x0000) return 0x00
            if (count == 0xFFFF) return 0x11

            return ((ln(count.toDouble()) / ln(2.0)) + 1).toInt()
        }
    }
}