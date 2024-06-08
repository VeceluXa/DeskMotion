package com.danilovfa.deskmotion.di

import com.danilovfa.deskmotion.utils.FileUtils
import org.koin.dsl.module

actual val nativeModule = module {
    factory { FileUtils(get()) }
}