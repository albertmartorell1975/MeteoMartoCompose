package com.martorell.albert.meteomartocompose.usecases.cityweather

import com.martorell.albert.meteomartocompose.data.auth.repositories.cityweather.LocationRepository
import javax.inject.Inject

data class SaveLocationUseCase @Inject constructor(private val locationRepository: LocationRepository) {

    suspend fun invoke(latitude: Double?, longitude: Double?) {
        locationRepository.saveLocation(
            latitude = latitude,
            longitude = longitude
        )
    }

}