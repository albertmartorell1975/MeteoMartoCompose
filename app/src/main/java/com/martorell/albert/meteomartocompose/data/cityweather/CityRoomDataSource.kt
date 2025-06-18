package com.martorell.albert.meteomartocompose.data.cityweather

import com.martorell.albert.meteomartocompose.data.auth.sources.cityweather.CityWeatherLocalDataSource
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import com.martorell.albert.meteomartocompose.domain.cityweather.response.CityWeatherResponse
import com.martorell.albert.meteomartocompose.framework.db.MeteoMartoDatabase
import com.martorell.albert.meteomartocompose.utils.toDomain
import com.martorell.albert.meteomartocompose.utils.toRoom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CityRoomDataSource(db: MeteoMartoDatabase) : CityWeatherLocalDataSource {

    private val cityDao = db.cityDao()

    //val city = cityDao.getCity()
    override suspend fun addCity(cityServer: CityWeatherResponse) {
        withContext(Dispatchers.IO) {
            cityDao.insert(cityServer.toRoom())
        }
    }

    override suspend fun loadCity(name: String): CityWeatherDomain =
        withContext(Dispatchers.IO) { cityDao.getCityByName(name).toDomain() }

    override suspend fun updateFavorite(cityWeatherDomain: CityWeatherDomain) {
        withContext(Dispatchers.IO){
            cityDao.insert(cityWeatherDomain.toRoom())
        }
    }
}