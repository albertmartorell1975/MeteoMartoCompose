package com.martorell.albert.meteomartocompose.usecases.cityweather

import com.martorell.albert.meteomartocompose.data.ResultResponse
import com.martorell.albert.meteomartocompose.data.auth.repositories.cityweather.LocationRepository
import com.martorell.albert.meteomartocompose.domain.cityweather.CurrentLocationDomain
import javax.inject.Inject

class CurrentLocationUseCase @Inject constructor(private val locationRepository: LocationRepository) {

    suspend fun invoke(
    ): ResultResponse<CurrentLocationDomain> =
        locationRepository.loadCurrentLocation()

}