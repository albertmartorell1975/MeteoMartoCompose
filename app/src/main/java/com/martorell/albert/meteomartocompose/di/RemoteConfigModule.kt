package com.martorell.albert.meteomartocompose.di

import android.content.Context
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.data.remoteconfig.RemoteConfigRepository
import com.martorell.albert.meteomartocompose.data.remoteconfig.RemoteConfigRepositoryImpl
import com.martorell.albert.meteomartocompose.data.remoteconfig.mappers.RemoteConfigMapper
import com.martorell.albert.meteomartocompose.data.remoteconfig.sources.RemoteConfigDataSource
import com.martorell.albert.meteomartocompose.framework.remoteconfig.FirebaseRemoteConfigDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteConfigProvideModule {

    @Provides
    @Singleton
    fun provideRemoteConfigMapper(@ApplicationContext context: Context): RemoteConfigMapper {
        return RemoteConfigMapper(
            defaultThreshold = context.getString(R.string.default_temp_threshold).toDoubleOrNull() ?: 30.0,
            minLimit = context.getString(R.string.min_temp_limit).toDoubleOrNull() ?: -100.0,
            maxLimit = context.getString(R.string.max_temp_limit).toDoubleOrNull() ?: 100.0
        )
    }

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
