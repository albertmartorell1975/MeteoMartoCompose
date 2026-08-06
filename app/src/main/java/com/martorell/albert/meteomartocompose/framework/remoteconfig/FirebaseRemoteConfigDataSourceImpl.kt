package com.martorell.albert.meteomartocompose.framework.remoteconfig

import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.martorell.albert.meteomartocompose.data.remoteconfig.sources.RemoteConfigDataSource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FirebaseRemoteConfigDataSourceImpl @Inject constructor(
    private val firebaseRemoteConfig: FirebaseRemoteConfig
) : RemoteConfigDataSource {

    companion object {
        private const val TEMPERATURE_THRESHOLD_KEY = "temperature_threshold"
    }

    override fun getTemperatureThreshold(): Flow<Double> = callbackFlow {
        // Send current value immediately
        trySend(firebaseRemoteConfig.getDouble(TEMPERATURE_THRESHOLD_KEY))

        val listener = object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                if (configUpdate.updatedKeys.contains(TEMPERATURE_THRESHOLD_KEY)) {
                    firebaseRemoteConfig.activate().addOnCompleteListener {
                        trySend(firebaseRemoteConfig.getDouble(TEMPERATURE_THRESHOLD_KEY))
                    }
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                // Handle error if needed
            }
        }

        val registration = firebaseRemoteConfig.addOnConfigUpdateListener(listener)
        
        awaitClose { 
            registration.remove()
        }
    }
}
