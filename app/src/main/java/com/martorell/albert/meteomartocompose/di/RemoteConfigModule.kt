package com.martorell.albert.meteomartocompose.di

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.data.remoteconfig.RemoteConfigRepository
import com.martorell.albert.meteomartocompose.data.remoteconfig.RemoteConfigRepositoryImpl
import com.martorell.albert.meteomartocompose.data.remoteconfig.sources.RemoteConfigDataSource
import com.martorell.albert.meteomartocompose.framework.remoteconfig.FirebaseRemoteConfigDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteConfigModule {

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

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig {
            val remoteConfig = FirebaseRemoteConfig.getInstance()
            val configSettings = remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0
            }
            remoteConfig.setConfigSettingsAsync(configSettings)
            remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
            remoteConfig.fetchAndActivate()
            return remoteConfig
        }
    }
}
