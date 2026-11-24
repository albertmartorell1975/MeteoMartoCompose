package com.martorell.albert.meteomartocompose.data.cityweather

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.martorell.albert.meteomartocompose.data.city.sources.LocationServerDataSource
import com.martorell.albert.meteomartocompose.data.util.ErrorMapper
import com.martorell.albert.meteomartocompose.domain.cityweather.CurrentLocationDomain
import com.martorell.albert.meteomartocompose.domain.model.DomainError
import com.martorell.albert.meteomartocompose.domain.model.Result
import com.martorell.albert.meteomartocompose.utils.toDomain
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class LocationServerDataSourceImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val fusedLocationProviderManager: FusedLocationProviderClient,
    private val errorMapper: ErrorMapper,
) : LocationServerDataSource {

    override suspend fun loadCurrentLocation(): Result<CurrentLocationDomain, DomainError> {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return Result.Error(DomainError.Location.PermissionDenied)
        }

        return try {
            val task = fusedLocationProviderManager.lastLocation
            suspendCancellableCoroutine { continuation ->
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
}
