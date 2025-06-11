package com.martorell.albert.meteomartocompose.data.cityweather

import com.martorell.albert.meteomartocompose.data.ResultResponse
import com.martorell.albert.meteomartocompose.data.auth.repositories.cityweather.LocationRepository
import com.martorell.albert.meteomartocompose.data.auth.sources.cityweather.LocationDataSource
import com.martorell.albert.meteomartocompose.domain.cityweather.CurrentLocationDomain
import jakarta.inject.Inject

class LocationRepositoryImpl @Inject constructor(private val locationDataSource: LocationDataSource) :
    LocationRepository {

    override suspend fun loadCurrentLocation(): ResultResponse<CurrentLocationDomain> {

        val result = locationDataSource.loadCurrentLocation()

        // Only if the result is correct, will be saved on the local database
        result.fold(
            {},
            {
                if (it != null) {
                    //authLocalSource.newUser(it)
                }
            }
        )

        return result

    }

}