package com.danilovfa.deskmotion.library.connection

import com.danilovfa.deskmotion.model.TransferEvent
import kotlinx.coroutines.flow.Flow

interface DataServer {
    suspend fun sendData(data: TransferEvent)
    fun receiveData(): Flow<TransferEvent>
    suspend fun disconnect()

    companion object {
        const val TAG = "DeskMotionDataServer"
    }
}