package com.danilovfa.deskmotion.receiver.data.di

import com.danilovfa.deskmotion.receiver.data.repository.DeskMotionRepositoryImpl
import com.danilovfa.deskmotion.receiver.data.repository.UserRepositoryImpl
import com.danilovfa.deskmotion.receiver.domain.repository.DeskMotionRepository
import com.danilovfa.deskmotion.receiver.domain.repository.UserRepository
import org.koin.dsl.module

val receiverDataModule = module {
    single<DeskMotionRepository> { DeskMotionRepositoryImpl(database = get()) }
    single<UserRepository> { UserRepositoryImpl(database = get()) }
}