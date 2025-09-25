package com.martorell.albert.meteomartocompose.data.city.sources

import com.martorell.albert.meteomartocompose.data.ResultResponse
import com.martorell.albert.meteomartocompose.domain.cityweather.CurrentLocationDomain

interface LocationServerDataSource {

    suspend fun loadCurrentLocation(): ResultResponse<CurrentLocationDomain>

}