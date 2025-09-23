package com.martorell.albert.meteomartocompose.data.city.sources

interface LocationLocalDataSource {

    suspend fun saveLocation(latitude: Double?, longitude: Double?)

}