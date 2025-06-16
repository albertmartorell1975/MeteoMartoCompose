package com.martorell.albert.meteomartocompose.data.auth.sources.cityweather

interface LocationLocalDataSource {

    suspend fun saveLocation(latitude: Double?, longitude: Double?)

}