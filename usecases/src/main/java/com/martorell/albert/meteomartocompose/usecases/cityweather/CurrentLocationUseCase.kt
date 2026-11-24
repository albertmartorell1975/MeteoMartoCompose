package com.martorell.albert.meteomartocompose.usecases.cityweather

import com.martorell.albert.meteomartocompose.data.city.repositories.LocationRepository
import com.martorell.albert.meteomartocompose.domain.cityweather.CurrentLocationDomain
import com.martorell.albert.meteomartocompose.domain.model.DomainError
import com.martorell.albert.meteomartocompose.domain.model.Result
import javax.inject.Inject

class CurrentLocationUseCase @Inject constructor(private val locationRepository: LocationRepository) {

    suspend operator fun invoke(): Result<CurrentLocationDomain, DomainError> =
        locationRepository.loadCurrentLocation()

}