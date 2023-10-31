package com.danilovfa.deskmotion.di

import kotlinx.coroutines.Dispatchers

object DispatchersProvider {
    val io get() = Dispatchers.IO
    val default get() = Dispatchers.Default
    val main get() = Dispatchers.Main
}