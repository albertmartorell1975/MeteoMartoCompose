package com.martorell.albert.meteomartocompose.data.city.repositories

import com.martorell.albert.meteomartocompose.data.ResultResponse
import com.martorell.albert.meteomartocompose.domain.cityweather.CurrentLocationDomain

interface LocationRepository {

    suspend fun loadCurrentLocation(): ResultResponse<CurrentLocationDomain>
    suspend fun saveLocation(latitude: Double?, longitude: Double?)

}