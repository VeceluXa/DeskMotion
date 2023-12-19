package com.danilovfa.deskmotion.model

import com.danilovfa.deskmotion.utils.format.toFormattedString
import kotlinx.serialization.Serializable

@Serializable
sealed class TransferEvent {
    abstract val epochMillis: Long

    @Serializable
    data class Data(val dx: Double, val dy: Double, val dz: Double, override val epochMillis: Long) : TransferEvent() {
        override fun toString(): String {
            return "dx=${dx.toFormattedString()}, dy=${dy.toFormattedString()}, dz=${dz.toFormattedString()}"
        }
    }

    @Serializable
    data class Start(override val epochMillis: Long) : TransferEvent() {
        override fun toString(): String {
            return "Started transferring events."
        }
    }

    @Serializable
    data class End(override val epochMillis: Long) : TransferEvent() {
        override fun toString(): String {
            return "Stopped transferring events."
        }
    }
}
