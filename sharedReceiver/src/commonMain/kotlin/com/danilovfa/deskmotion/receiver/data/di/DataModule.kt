package com.danilovfa.deskmotion.receiver.data.di

import com.danilovfa.deskmotion.receiver.data.repository.DeskMotionRepositoryImpl
import com.danilovfa.deskmotion.receiver.domain.repository.DeskMotionRepository
import org.koin.dsl.module

val receiverDataModule = module {
    factory<DeskMotionRepository> {
        DeskMotionRepositoryImpl(get())
    }
}