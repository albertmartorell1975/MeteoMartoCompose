package com.martorell.albert.meteomartocompose.data.auth.sources.cityweather

import com.martorell.albert.meteomartocompose.data.ResultResponse
import com.martorell.albert.meteomartocompose.domain.cityweather.CurrentLocationDomain

interface LocationServerDataSource {

    suspend fun loadCurrentLocation(): ResultResponse<CurrentLocationDomain>

}