package com.martorell.albert.meteomartocompose.data.cityweather

import android.annotation.SuppressLint
import arrow.core.Either
import arrow.core.right
import com.google.android.gms.location.FusedLocationProviderClient
import com.martorell.albert.meteomartocompose.data.CustomError
import com.martorell.albert.meteomartocompose.data.ResultResponse
import com.martorell.albert.meteomartocompose.data.auth.sources.cityweather.LocationServerDataSource
import com.martorell.albert.meteomartocompose.data.customTryCatch
import com.martorell.albert.meteomartocompose.domain.cityweather.CurrentLocationDomain
import com.martorell.albert.meteomartocompose.utils.toDomain
import jakarta.inject.Inject
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

@SuppressLint("MissingPermission")
class LocationServerDataSourceImpl @Inject constructor(
    private val fusedLocationProviderManager: FusedLocationProviderClient
) : LocationServerDataSource {

    override suspend fun loadCurrentLocation(): ResultResponse<CurrentLocationDomain> =

        customTryCatch {

            return suspendCancellableCoroutine { continuation ->

                fusedLocationProviderManager.lastLocation.apply {

                    if (isComplete) {

                        if (isSuccessful) {
                            continuation.resume(result.toDomain().right())
                        } else {
                            continuation.resume(Either.Left(CustomError.Unknown("Location could not be determined")))
                        }

                        return@suspendCancellableCoroutine
                    }

                    addOnSuccessListener {

                        if (result != null) {
                            continuation.resume(result.toDomain().right())
                        } else {
                            continuation.resume(Either.Left(CustomError.Unknown("Location could not be determined")))
                        }

                    }

                    addOnFailureListener {
                        continuation.resume(Either.Left(CustomError.Unknown("Location could not be determined")))
                    }

                    addOnCanceledListener {
                        continuation.cancel()
                    }

                }

            }

        }

}