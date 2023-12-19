package com.danilovfa.deskmotion.di

import com.danilovfa.deskmotion.library.connection.socket.SocketManager
import org.koin.dsl.module

val sharedModule = module {
    factory { params ->
        SocketManager(params.get())
    }
}