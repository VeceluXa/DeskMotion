package com.danilovfa.deskmotion.sender.library.sensors

import com.danilovfa.deskmotion.model.TransferEvent
import com.danilovfa.deskmotion.utils.time.currentTime

data class AccelerometerData(val dx: Float, val dy: Float, val dz: Float) {
    fun toTransferEvent(): TransferEvent {
        return TransferEvent.Data(
            dx = dx.toDouble(),
            dy = dy.toDouble(),
            dz = dz.toDouble(),
            epochMillis = currentTime()
        )
    }
}