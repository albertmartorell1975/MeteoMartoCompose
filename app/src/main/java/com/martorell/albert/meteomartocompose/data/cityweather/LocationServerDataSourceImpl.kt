package com.martorell.albert.meteomartocompose.data.cityweather

import android.annotation.SuppressLint
import com.google.android.gms.location.FusedLocationProviderClient
import com.martorell.albert.meteomartocompose.data.city.sources.LocationServerDataSource
import com.martorell.albert.meteomartocompose.data.util.ErrorMapper
import com.martorell.albert.meteomartocompose.domain.cityweather.CurrentLocationDomain
import com.martorell.albert.meteomartocompose.domain.model.DomainError
import com.martorell.albert.meteomartocompose.domain.model.Result
import com.martorell.albert.meteomartocompose.utils.toDomain
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

@SuppressLint("MissingPermission")
class LocationServerDataSourceImpl @Inject constructor(
    private val fusedLocationProviderManager: FusedLocationProviderClient,
    private val errorMapper: ErrorMapper,
) : LocationServerDataSource {

    override suspend fun loadCurrentLocation(): Result<CurrentLocationDomain, DomainError> =
        try {
            suspendCancellableCoroutine { continuation ->
                val task = fusedLocationProviderManager.lastLocation
                if (task.isComplete) {
                    when {
                        task.isSuccessful -> {
                            val location = task.result
                            if (location != null) {
                                continuation.resume(Result.Success(location.toDomain()))
                            } else {
                                continuation.resume(Result.Error(DomainError.Location.Unavailable))
                            }
                        }

                        task.isCanceled -> continuation.cancel()
                        else -> {
                            val error = task.exception?.let(errorMapper::map)
                                ?: DomainError.Location.Unavailable
                            continuation.resume(Result.Error(error))
                        }
                    }
                    return@suspendCancellableCoroutine
                }

                task.addOnSuccessListener { location ->
                    if (continuation.isActive) {
                        if (location != null) {
                            continuation.resume(Result.Success(location.toDomain()))
                        } else {
                            continuation.resume(Result.Error(DomainError.Location.Unavailable))
                        }
                    }
                }

                task.addOnFailureListener { exception ->
                    if (continuation.isActive) {
                        continuation.resume(Result.Error(errorMapper.map(exception)))
                    }
                }

                task.addOnCanceledListener {
                    if (continuation.isActive) {
                        continuation.cancel()
                    }
                }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.Error(errorMapper.map(e))
        }
}
