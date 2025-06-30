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

}