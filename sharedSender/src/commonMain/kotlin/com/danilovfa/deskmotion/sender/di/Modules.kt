package com.danilovfa.deskmotion.sender.di

import com.danilovfa.deskmotion.di.sharedModules
import org.koin.core.module.Module

val sharedSenderModules = sharedModules + listOf(senderModule)

expect val senderModule: Module