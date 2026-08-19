package com.martorell.albert.meteomartocompose.usecases.cityweather

import com.martorell.albert.meteomartocompose.data.auth.repositories.cityweather.LocationRepository
import javax.inject.Inject

class SaveLocationUseCase @Inject constructor(private val locationRepository: LocationRepository) {

    operator suspend fun invoke(latitude: Double?, longitude: Double?) {
        locationRepository.saveLocation(
            latitude = latitude,
            longitude = longitude
        )
    }

}