package com.danilovfa.deskmotion.library.datastore

import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import kotlinx.coroutines.CoroutineScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import com.danilovfa.deskmotion.utils.FileUtils
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import java.io.File

object DataStoreFactory : KoinComponent {
    private val fileUtils by inject<FileUtils>()

    fun <T> create(
        defaultValue: T,
        dataStoreName: String,
        serializer: KSerializer<T>,
        corruptionHandler: ReplaceFileCorruptionHandler<T>? = null,
        coroutineScope: CoroutineScope,
        migrations: List<DataMigration<T>>
    ): DataStore<T> {
        return DataStoreFactory.create(
            serializer = DataStoreSerializer(serializer, defaultValue),
            corruptionHandler = corruptionHandler,
            migrations = migrations,
            scope = coroutineScope,
            produceFile = {
                File(fileUtils.getFilesDirPath(), dataStoreName)
            }
        )
    }
}