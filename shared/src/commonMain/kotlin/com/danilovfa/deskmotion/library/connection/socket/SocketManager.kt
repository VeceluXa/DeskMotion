package com.danilovfa.deskmotion.library.connection.socket

import co.touchlab.kermit.Logger
import com.danilovfa.deskmotion.library.connection.DataServer
import com.danilovfa.deskmotion.library.connection.DataServer.Companion.TAG
import com.danilovfa.deskmotion.model.TransferEvent
import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import io.ktor.util.moveToByteArray
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.close
import io.ktor.utils.io.consumeEachBufferRange
import io.ktor.utils.io.writeAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SocketManager(
    val isServer: Boolean = false
): DataServer {
    private val selectorManager = SelectorManager(Dispatchers.IO)
    private var socket: Socket? = null

    private var writeChannel: ByteWriteChannel? = null
    private var readChannel: ByteReadChannel? = null

    suspend fun connect(ip: String, port: Int) {
        Logger.i { "Try to connect" }
        socket = if (isServer) {
            aSocket(selectorManager)
                .tcp()
                .bind(ip, port)
                .accept()
        } else {
            aSocket(selectorManager)
                .tcp()
                .connect(ip, port)
        }

        Logger.i { "Connected" }
    }

    override suspend fun sendData(data: TransferEvent) {
        if (writeChannel == null) {
            writeChannel = socket?.openWriteChannel(autoFlush = true) ?: throw Exception("")
        }

        val encodedData = Json.encodeToString(data)
        encodedData.encodeToByteArray()
        writeChannel?.run {
            val byteArray = encodedData.encodeToByteArray()
            writeAvailable(byteArray)
        }
    }

    override fun receiveData(): Flow<TransferEvent> {
        Logger.i { "Try to create a Read Channel" }
        if (readChannel == null) {
            readChannel = socket?.openReadChannel()
        }

        return flow {
            if (readChannel == null) {
                throw Exception("Socket is null")
            }

            while(true) {
                readChannel?.consumeEachBufferRange { buffer, last ->
                    val line = buffer.moveToByteArray().decodeToString()
                    try {
                        val decodedData = Json.decodeFromString(TransferEvent.serializer(), line)
                        emit(decodedData)
                    } catch (e: Exception) {
//                        Logger.e(TAG, e)
                    }

                    true
                }
            }
        }
            .filterNotNull()
            .flowOn(Dispatchers.IO)
    }

    override suspend fun disconnect() {
        withContext(Dispatchers.IO) {
            writeChannel?.close()
            readChannel = null
            socket?.close()
            selectorManager.close()
        }
    }
}