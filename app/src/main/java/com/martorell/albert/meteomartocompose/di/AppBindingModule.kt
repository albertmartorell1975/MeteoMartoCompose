package com.martorell.albert.meteomartocompose.di

import com.martorell.albert.meteomartocompose.data.city.repositories.PermissionChecker
import com.martorell.albert.meteomartocompose.data.city.repositories.PermissionRepository
import com.martorell.albert.meteomartocompose.data.cityweather.PermissionRepositoryImpl
import com.martorell.albert.meteomartocompose.data.preferences.UserPreferences
import com.martorell.albert.meteomartocompose.framework.preferences.UserPreferencesImpl
import com.martorell.albert.meteomartocompose.utils.AndroidPermissionChecker
import com.martorell.albert.meteomartocompose.utils.AppLifecycleObserver
import com.martorell.albert.meteomartocompose.utils.AppLifecycleObserverImpl
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
        impl: PermissionRepositoryImpl,
    ): PermissionRepository

    @Binds
    @Singleton
    abstract fun bindPermissionChecker(
        impl: AndroidPermissionChecker,
    ): PermissionChecker

    @Binds
    @Singleton
    abstract fun bindAppLifecycleObserver(
        impl: AppLifecycleObserverImpl,
    ): AppLifecycleObserver

    @Binds
    @Singleton
    abstract fun bindUserPreferences(
        impl: UserPreferencesImpl,
    ): UserPreferences
}
