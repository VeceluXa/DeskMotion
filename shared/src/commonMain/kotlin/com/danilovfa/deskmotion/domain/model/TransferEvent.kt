package com.danilovfa.deskmotion.domain.model

sealed class TransferEvent {
    data class Data(val dx: Double, val dy: Double, val dz: Double) : TransferEvent()
    data object Start : TransferEvent()
    data object End : TransferEvent()
}
