package com.martorell.albert.meteomartocompose.data.cityweather

import androidx.room.withTransaction
import com.martorell.albert.meteomartocompose.BuildConfig
import com.martorell.albert.meteomartocompose.data.city.sources.CityWeatherLocalDataSource
import com.martorell.albert.meteomartocompose.data.util.ErrorMapper
import com.martorell.albert.meteomartocompose.data.util.safeCall
import com.martorell.albert.meteomartocompose.domain.cityweather.CityWeatherDomain
import com.martorell.albert.meteomartocompose.domain.cityweather.response.CityWeatherResponse
import com.martorell.albert.meteomartocompose.domain.model.DomainError
import com.martorell.albert.meteomartocompose.domain.model.Result
import com.martorell.albert.meteomartocompose.framework.db.MeteoMartoDatabase
import com.martorell.albert.meteomartocompose.utils.listToDomain
import com.martorell.albert.meteomartocompose.utils.openWeatherConverter
import com.martorell.albert.meteomartocompose.utils.toDomain
import com.martorell.albert.meteomartocompose.utils.toRoom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CityRoomDataSource @Inject constructor(
    private val db: MeteoMartoDatabase,
    private val errorMapper: ErrorMapper,
) : CityWeatherLocalDataSource {

    private val cityDao = db.cityDao()

    override suspend fun isEmpty(): Boolean = cityDao.cityCount() == 0
    override suspend fun addCity(cityServer: CityWeatherResponse) {

        withContext(Dispatchers.IO) {

            cityDao.insert(cityServer.toRoom())

        }

    }

    override suspend fun refreshCity(cityServer: CityWeatherResponse) {
        withContext(Dispatchers.IO) {
            db.withTransaction {
                if (isEmpty()) {
                    cityDao.insert(cityServer.toRoom())
                } else {
                    cityDao.allCitiesAsNotJustAdded()
                    val cityInfo = cityDao.getCityByName(cityServer.name)
                    if (cityInfo == null) {
                        cityDao.insert(cityServer.toRoom())
                    } else {
                        cityDao.update(
                            name = cityServer.name,
                            weatherDescription = if (cityServer.weather.isNotEmpty()) cityServer.weather[0].description else "",
                            weatherIcon = if (cityServer.weather.isNotEmpty()) "${BuildConfig.OPEN_WEATHER_ICON_URL}${cityServer.weather[0].icon}@2x.png" else "",
                            pressure = cityServer.main.pressure,
                            temperatureMax = cityServer.main.temp_max.openWeatherConverter(),
                            temperatureMin = cityServer.main.temp_min.openWeatherConverter(),
                            temperature = cityServer.main.temp.openWeatherConverter(),
                        )
                    }
                }
            }
        }
    }

    override suspend fun updateCity(
        cityName: String,
        weatherDescription: String?,
        weatherIcon: String?,
        pressure: Int,
        temperatureMax: Double,
        temperatureMin: Double,
        temperature: Double
    ) {
        withContext(Dispatchers.IO) {
            cityDao.update(
                name = cityName,
                weatherDescription = weatherDescription,
                weatherIcon = weatherIcon,
                pressure = pressure,
                temperatureMax = temperatureMax,
                temperatureMin = temperatureMin,
                temperature = temperature,
            )
        }
    }

    override suspend fun makeAllCitiesAsNotJustAdded() {

        withContext(Dispatchers.IO) {

            if (!isEmpty())
                cityDao.allCitiesAsNotJustAdded()

        }

    }

    override suspend fun loadCity(name: String): Result<CityWeatherDomain, DomainError> {
        val queryResult = safeCall(mapper = errorMapper::map) {
            withContext(Dispatchers.IO) { cityDao.getCityByName(name) }
        }
        return when (queryResult) {
            is Result.Error -> queryResult
            is Result.Success -> {
                val city = queryResult.data
                if (city == null) {
                    Result.Error(DomainError.Local.NotFound)
                } else {
                    Result.Success(city.toDomain())
                }
            }
        }
    }
    
    override suspend fun updateFavorite(cityWeatherDomain: CityWeatherDomain) {

        withContext(Dispatchers.IO) {

            cityDao.insert(cityWeatherDomain.toRoom())

        }

    }

    override fun getAll(): Flow<List<CityWeatherDomain>> =
        cityDao.getAll().map { it.listToDomain() }

    override suspend fun isCurrentCityFavorite(): Boolean =

        withContext(Dispatchers.IO) {

            if (!isEmpty())
                if (cityDao.loadCurrentCity() != null) {
                    return@withContext cityDao.loadCurrentCity()!!.favorite
                } else
                    return@withContext false
            else
                return@withContext false

        }

    override suspend fun removeCityAsFavorite(cityName: String) {

        withContext(Dispatchers.IO) {

            cityDao.removeCityAsFavorite(cityName)

        }

    }

    override suspend fun updateAlertStatus(name: String, isNotified: Boolean) {
        withContext(Dispatchers.IO) {
            cityDao.updateAlertStatus(name, isNotified)
        }
    }

}