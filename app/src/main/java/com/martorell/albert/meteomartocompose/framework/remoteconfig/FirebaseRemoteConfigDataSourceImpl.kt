package com.martorell.albert.meteomartocompose.framework.remoteconfig

import android.util.Log
import arrow.core.left
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.martorell.albert.meteomartocompose.data.ResultResponse
import com.martorell.albert.meteomartocompose.data.customTryCatch
import com.martorell.albert.meteomartocompose.data.remoteconfig.sources.RemoteConfigDataSource
import com.martorell.albert.meteomartocompose.data.toCustomError
import com.martorell.albert.meteomartocompose.utils.AppConstants
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FirebaseRemoteConfigDataSourceImpl @Inject constructor(
    private val firebaseRemoteConfig: FirebaseRemoteConfig
) : RemoteConfigDataSource {

    companion object {
        private const val TAG = AppConstants.DEBUG_TAG
        private const val TEMPERATURE_THRESHOLD_KEY = "temperature_threshold"
        private const val WEATHER_CHECK_INTERVAL_KEY = "weather_check_interval_minutes"
    }

    override fun getTemperatureThreshold(): Flow<ResultResponse<Double>> = callbackFlow {
        Log.d(TAG, "[RemoteConfig] Starting threshold flow...")
        // Send current value immediately
        trySend(customTryCatch { firebaseRemoteConfig.getDouble(TEMPERATURE_THRESHOLD_KEY) })

        val listener = object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                Log.d(TAG, "[RemoteConfig] Update detected! Keys: ${configUpdate.updatedKeys}")
                if (configUpdate.updatedKeys.contains(TEMPERATURE_THRESHOLD_KEY)) {
                    firebaseRemoteConfig.activate().addOnCompleteListener {
                        val newValue = firebaseRemoteConfig.getDouble(TEMPERATURE_THRESHOLD_KEY)
                        Log.d(TAG, "[RemoteConfig] New Threshold activated: $newValue")
                        trySend(customTryCatch { newValue })
                    }
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                Log.e(TAG, "[RemoteConfig] Error in listener", error)
                trySend(error.toCustomError().left())
            }
        }

        val registration = firebaseRemoteConfig.addOnConfigUpdateListener(listener)

        awaitClose {
            Log.d(TAG, "Closing getTemperatureThreshold flow and removing listener")
            registration.remove()
        }
    }

    override fun getWeatherCheckInterval(): Flow<ResultResponse<Long>> = callbackFlow {
        // Send current value immediately
        trySend(customTryCatch { firebaseRemoteConfig.getLong(WEATHER_CHECK_INTERVAL_KEY) })

        val listener = object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                if (configUpdate.updatedKeys.contains(WEATHER_CHECK_INTERVAL_KEY)) {
                    firebaseRemoteConfig.activate().addOnCompleteListener {
                        trySend(customTryCatch { firebaseRemoteConfig.getLong(WEATHER_CHECK_INTERVAL_KEY) })
                    }
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                Log.e(TAG, "ConfigUpdateListener error (Interval)", error)
                trySend(error.toCustomError().left())
            }
        }

        val registration = firebaseRemoteConfig.addOnConfigUpdateListener(listener)

        awaitClose {
            registration.remove()
        }
    }
}
