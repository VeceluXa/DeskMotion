// Workaround for https://youtrack.jetbrains.com/issue/KT-21186
@file:JvmName("ReceiverModuleJvm")

package com.danilovfa.deskmotion.receiver.di

import com.danilovfa.deskmotion.receiver.deskMotionDatabase.DeskMotionDatabase
import com.danilovfa.deskmotion.receiver.data.database.DriverFactory
import com.danilovfa.deskmotion.receiver.data.database.adapter.listOfCoordinatesAdapter
import deskMotionDatabase.LevelEntity
import deskMotionDatabase.PlayLogEntity
import org.koin.dsl.module

actual val receiverModule = module {
    single {
        DriverFactory().createDriver()
    }

    single {
        DeskMotionDatabase(
            driver = get(),
            levelEntityAdapter = LevelEntity.Adapter(listOfCoordinatesAdapter),
            playLogEntityAdapter = PlayLogEntity.Adapter(listOfCoordinatesAdapter)
        )
    }
}