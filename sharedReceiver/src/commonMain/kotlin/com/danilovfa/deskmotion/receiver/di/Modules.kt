package com.danilovfa.deskmotion.receiver.di

import com.danilovfa.deskmotion.di.sharedModules
import com.danilovfa.deskmotion.receiver.data.di.receiverDataModule
import org.koin.core.module.Module

val sharedReceiverModules = sharedModules + listOf(
    receiverModule,
    receiverDataModule
)

expect val receiverModule: Module