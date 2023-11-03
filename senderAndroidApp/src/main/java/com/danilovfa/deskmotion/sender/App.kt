package com.danilovfa.deskmotion.sender

import android.app.Application
import com.danilovfa.deskmotion.sender.di.sharedSenderModules
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(sharedSenderModules)
            printLogger(Level.DEBUG)
        }
    }
}