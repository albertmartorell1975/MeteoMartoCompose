package com.martorell.albert.meteomartocompose.data.city

import com.martorell.albert.meteomartocompose.domain.cityweather.response.CityWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CityWeatherServerDataSource {

    @GET("data/2.5/weather?")
    suspend fun getWeather(@Query("lat") lat: String, @Query("lon") lon: String): CityWeatherResponse

}