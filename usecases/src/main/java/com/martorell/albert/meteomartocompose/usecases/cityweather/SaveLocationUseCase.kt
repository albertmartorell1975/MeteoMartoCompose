package com.martorell.albert.meteomartocompose.usecases.cityweather

import com.martorell.albert.meteomartocompose.data.city.repositories.LocationRepository
import javax.inject.Inject

class SaveLocationUseCase @Inject constructor(private val locationRepository: LocationRepository) {

    suspend operator fun invoke(latitude: Double?, longitude: Double?) {
        locationRepository.saveLocation(
            latitude = latitude,
            longitude = longitude
        )
    }

}