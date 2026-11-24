package com.martorell.albert.meteomartocompose.data.city.repositories

import com.martorell.albert.meteomartocompose.domain.cityweather.CurrentLocationDomain
import com.martorell.albert.meteomartocompose.domain.model.DomainError
import com.martorell.albert.meteomartocompose.domain.model.Result

interface LocationRepository {

    suspend fun loadCurrentLocation(): Result<CurrentLocationDomain, DomainError>
    suspend fun saveLocation(latitude: Double?, longitude: Double?)

}