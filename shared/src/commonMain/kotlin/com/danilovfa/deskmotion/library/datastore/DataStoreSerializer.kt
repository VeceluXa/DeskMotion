package com.danilovfa.deskmotion.library.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.koin.ext.getFullName
import java.io.InputStream
import java.io.OutputStream

class DataStoreSerializer<T>(
    private val serializer: KSerializer<T>,
    override val defaultValue: T
) : Serializer<T> {
    override suspend fun readFrom(input: InputStream): T {
        try {
            return Json.decodeFromString(
                serializer,
                input.readBytes().decodeToString()
            )
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read ${serializer::class.getFullName()}")
        }
    }

    override suspend fun writeTo(t: T, output: OutputStream) {
        return output.write(
            Json.encodeToString(serializer, t).encodeToByteArray()
        )
    }
}