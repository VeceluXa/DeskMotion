// Workaround for https://youtrack.jetbrains.com/issue/KT-21186
@file:JvmName("ReceiverModuleJvm")

package com.danilovfa.deskmotion.receiver.di

import com.danilovfa.deskmotion.receiver.deskMotionDatabase.DeskMotionDatabase
import com.danilovfa.deskmotion.receiver.data.database.DriverFactory
import com.danilovfa.deskmotion.receiver.data.database.adapter.listOfCoordinatesAdapter
import com.danilovfa.deskmotion.receiver.data.database.adapter.localDateAdapter
import deskMotionDatabase.LevelEntity
import deskMotionDatabase.PlayLogEntity
import deskMotionDatabase.UserEntity
import org.koin.dsl.module

actual val receiverModule = module {
    single {
        DriverFactory(get()).createDriver()
    }

    single {
        DeskMotionDatabase(
            driver = get(),
            levelEntityAdapter = LevelEntity.Adapter(listOfCoordinatesAdapter),
            userEntityAdapter = UserEntity.Adapter(localDateAdapter),
            playLogEntityAdapter = PlayLogEntity.Adapter(listOfCoordinatesAdapter)
        )
    }
}