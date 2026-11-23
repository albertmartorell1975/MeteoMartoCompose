package com.martorell.albert.meteomartocompose.framework.remoteconfig

import android.util.Log
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.martorell.albert.meteomartocompose.data.remoteconfig.sources.RemoteConfigDataSource
import com.martorell.albert.meteomartocompose.data.util.ErrorMapper
import com.martorell.albert.meteomartocompose.data.util.safeSyncCall
import com.martorell.albert.meteomartocompose.domain.model.DomainError
import com.martorell.albert.meteomartocompose.domain.model.Result
import com.martorell.albert.meteomartocompose.utils.AppConstants
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FirebaseRemoteConfigDataSourceImpl @Inject constructor(
    private val firebaseRemoteConfig: FirebaseRemoteConfig,
    private val errorMapper: ErrorMapper,
) : RemoteConfigDataSource {

    override fun getTemperatureThreshold(): Flow<Result<Double, DomainError>> = callbackFlow {
        Log.d(TAG, "[RemoteConfig] Starting threshold flow...")
        trySend(
            safeSyncCall(mapper = errorMapper::map) {
                firebaseRemoteConfig.getDouble(TEMPERATURE_THRESHOLD_KEY)
            }
        )

        val listener = object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                Log.d(TAG, "[RemoteConfig] Update detected! Keys: ${configUpdate.updatedKeys}")
                if (configUpdate.updatedKeys.contains(TEMPERATURE_THRESHOLD_KEY)) {
                    firebaseRemoteConfig.activate().addOnCompleteListener {
                        val result = safeSyncCall(mapper = errorMapper::map) {
                            firebaseRemoteConfig.getDouble(TEMPERATURE_THRESHOLD_KEY)
                        }
                        Log.d(TAG, "[RemoteConfig] New Threshold activated: $result")
                        trySend(result)
                    }
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                Log.e(TAG, "[RemoteConfig] Error in listener", error)
                trySend(Result.Error(errorMapper.map(error)))
            }
        }

        val registration = firebaseRemoteConfig.addOnConfigUpdateListener(listener)

        awaitClose {
            Log.d(TAG, "Closing getTemperatureThreshold flow and removing listener")
            registration.remove()
        }
    }

    override fun getWeatherCheckInterval(): Flow<Result<Long, DomainError>> = callbackFlow {
        trySend(
            safeSyncCall(mapper = errorMapper::map) {
                firebaseRemoteConfig.getLong(WEATHER_CHECK_INTERVAL_KEY)
            }
        )

        val listener = object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                if (configUpdate.updatedKeys.contains(WEATHER_CHECK_INTERVAL_KEY)) {
                    firebaseRemoteConfig.activate().addOnCompleteListener {
                        trySend(
                            safeSyncCall(mapper = errorMapper::map) {
                                firebaseRemoteConfig.getLong(WEATHER_CHECK_INTERVAL_KEY)
                            }
                        )
                    }
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                Log.e(TAG, "ConfigUpdateListener error (Interval)", error)
                trySend(Result.Error(errorMapper.map(error)))
            }
        }

        val registration = firebaseRemoteConfig.addOnConfigUpdateListener(listener)

        awaitClose {
            registration.remove()
        }
    }

    companion object {
        private const val TAG = AppConstants.DEBUG_TAG
        private const val TEMPERATURE_THRESHOLD_KEY = "temperature_threshold"
        private const val WEATHER_CHECK_INTERVAL_KEY = "weather_check_interval_minutes"
    }
}
