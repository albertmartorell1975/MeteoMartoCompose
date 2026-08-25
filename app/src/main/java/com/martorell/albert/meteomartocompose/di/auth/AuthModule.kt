package com.martorell.albert.meteomartocompose.di.auth

import com.martorell.albert.meteomartocompose.data.auth.AccountServiceImpl
import com.martorell.albert.meteomartocompose.data.auth.AuthRoomDataSource
import com.martorell.albert.meteomartocompose.data.auth.AuthRepositoryImpl
import com.martorell.albert.meteomartocompose.data.auth.repositories.AuthRepository
import com.martorell.albert.meteomartocompose.data.auth.sources.AccountService
import com.martorell.albert.meteomartocompose.data.auth.sources.AuthLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    @Singleton
    abstract fun bindAccountService(
        impl: AccountServiceImpl
    ): AccountService

    @Binds
    @Singleton
    abstract fun bindAuthLocalDataSource(
        impl: AuthRoomDataSource
    ): AuthLocalDataSource

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

}
