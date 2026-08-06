package com.martorell.albert.meteomartocompose.di

import com.martorell.albert.meteomartocompose.data.auth.repositories.cityweather.PermissionChecker
import com.martorell.albert.meteomartocompose.data.auth.repositories.cityweather.PermissionRepository
import com.martorell.albert.meteomartocompose.data.cityweather.PermissionRepositoryImpl
import com.martorell.albert.meteomartocompose.utils.AndroidPermissionChecker
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppBindingModule {

    @Binds
    @Singleton
    abstract fun bindPermissionRepository(
        impl: PermissionRepositoryImpl
    ): PermissionRepository

    @Binds
    @Singleton
    abstract fun bindPermissionChecker(
        impl: AndroidPermissionChecker
    ): PermissionChecker
}
