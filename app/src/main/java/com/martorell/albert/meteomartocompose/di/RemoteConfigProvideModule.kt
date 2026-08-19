package com.martorell.albert.meteomartocompose.di

import android.content.Context
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.data.remoteconfig.mappers.RemoteConfigMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteConfigProvideModule {

    private const val DEBUG_MINIMUM_FETCH_INTERVAL = 0L

    @Provides
    @Singleton
    fun provideRemoteConfigMapper(@ApplicationContext context: Context): RemoteConfigMapper {
        return RemoteConfigMapper(
            defaultThreshold = context.getString(R.string.default_temp_threshold).toDoubleOrNull()
                ?: RemoteConfigMapper.DEFAULT_THRESHOLD,
            minLimit = context.getString(R.string.min_temp_limit).toDoubleOrNull()
                ?: RemoteConfigMapper.MIN_TEMP_LIMIT,
            maxLimit = context.getString(R.string.max_temp_limit).toDoubleOrNull()
                ?: RemoteConfigMapper.MAX_TEMP_LIMIT,
            defaultInterval = context.getString(R.string.default_weather_check_interval).toLongOrNull()
                ?: RemoteConfigMapper.DEFAULT_INTERVAL,
            minInterval = context.getString(R.string.min_weather_check_interval).toLongOrNull()
                ?: RemoteConfigMapper.MIN_INTERVAL
        )
    }

    @Provides
    @Singleton
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = DEBUG_MINIMUM_FETCH_INTERVAL
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        remoteConfig.fetchAndActivate()
        return remoteConfig
    }
}
