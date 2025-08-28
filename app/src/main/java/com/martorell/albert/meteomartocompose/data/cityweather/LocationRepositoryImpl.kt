package com.martorell.albert.meteomartocompose.data.cityweather

import com.martorell.albert.meteomartocompose.data.ResultResponse
import com.martorell.albert.meteomartocompose.data.auth.repositories.cityweather.LocationRepository
import com.martorell.albert.meteomartocompose.data.auth.sources.cityweather.LocationLocalDataSource
import com.martorell.albert.meteomartocompose.data.auth.sources.cityweather.LocationServerDataSource
import com.martorell.albert.meteomartocompose.domain.cityweather.CurrentLocationDomain
import jakarta.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val locationServerDataSource: LocationServerDataSource,
    private val locationLocalDataSource: LocationLocalDataSource
) :
    LocationRepository {

    override suspend fun loadCurrentLocation(): ResultResponse<CurrentLocationDomain> {

        val result = locationServerDataSource.loadCurrentLocation()

        // Only if the result is correct, will be saved on the local database
        result.fold(
            {},
            {
                locationLocalDataSource.saveLocation(
                    latitude = it.latitude,
                    longitude = it.longitude
                )
            }
        )

        return result

    }

}