package com.martorell.albert.meteomartocompose.di

import com.martorell.albert.meteomartocompose.data.remoteconfig.RemoteConfigRepository
import com.martorell.albert.meteomartocompose.data.remoteconfig.RemoteConfigRepositoryImpl
import com.martorell.albert.meteomartocompose.data.remoteconfig.sources.RemoteConfigDataSource
import com.martorell.albert.meteomartocompose.framework.remoteconfig.FirebaseRemoteConfigDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteConfigBindingModule {

    @Binds
    @Singleton
    abstract fun bindRemoteConfigDataSource(
        firebaseRemoteConfigDataSourceImpl: FirebaseRemoteConfigDataSourceImpl
    ): RemoteConfigDataSource

    @Binds
    @Singleton
    abstract fun bindRemoteConfigRepository(
        remoteConfigRepositoryImpl: RemoteConfigRepositoryImpl
    ): RemoteConfigRepository
}
