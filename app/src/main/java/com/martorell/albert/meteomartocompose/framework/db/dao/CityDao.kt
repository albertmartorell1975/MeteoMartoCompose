package com.martorell.albert.meteomartocompose.framework.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.martorell.albert.meteomartocompose.framework.db.model.CityWeather
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {

    @Query("UPDATE CityWeather SET justAdded = 0")
    suspend fun allCitiesAsNotJustAdded()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(city: CityWeather)

    @Query(value = "SELECT * FROM CityWeather WHERE name = :name")
    suspend fun getCityByName(name: String): CityWeather

    @Query("SELECT * FROM CityWeather")
    fun getAll(): Flow<List<CityWeather>>

    @Query("DELETE FROM CityWeather")
    suspend fun cleanTable()

    @Query("SELECT COUNT(name) FROM CityWeather ")
    suspend fun cityCount(): Int

    @Query(value = "SELECT * FROM CityWeather WHERE justAdded = 1")
    suspend fun loadCurrentCity(): CityWeather?

    @Query(
        "UPDATE CityWeather SET weatherDescription = :weatherDescription," +
                "weatherIcon = :weatherIcon, pressure = :pressure, temperatureMax = :temperatureMax, " +
                "temperatureMin = :temperatureMin, temperature = :temperature,justAdded = 1  WHERE name =:name "
    )
    suspend fun update(
        name: String,
        weatherDescription: String?,
        weatherIcon: String?,
        pressure: Int,
        temperatureMax: Double,
        temperatureMin: Double,
        temperature: Double,
    )

    @Query(
        "UPDATE CityWeather SET favorite = false WHERE name =:name "
    )
    suspend fun removeCityAsFavorite(name: String)

}