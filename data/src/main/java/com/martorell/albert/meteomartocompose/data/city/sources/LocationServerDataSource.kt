package com.martorell.albert.meteomartocompose.data.city.sources

import com.martorell.albert.meteomartocompose.domain.cityweather.CurrentLocationDomain
import com.martorell.albert.meteomartocompose.domain.model.DomainError
import com.martorell.albert.meteomartocompose.domain.model.Result

interface LocationServerDataSource {

    suspend fun loadCurrentLocation(): Result<CurrentLocationDomain, DomainError>

}