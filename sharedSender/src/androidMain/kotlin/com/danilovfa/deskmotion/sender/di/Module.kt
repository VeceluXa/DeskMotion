package com.danilovfa.deskmotion.sender.di

import com.danilovfa.deskmotion.sender.library.sensors.Accelerometer
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val senderModule = module {
    factory {
        Accelerometer(androidContext())
    }
}