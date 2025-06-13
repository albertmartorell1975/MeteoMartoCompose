package com.martorell.albert.meteomartocompose.framework.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.martorell.albert.meteomartocompose.framework.db.model.CityWeather
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(city: CityWeather)

    @Query("SELECT * FROM CityWeather LIMIT 1")
    fun getCity(): CityWeather

    @Query(value = "SELECT * FROM CityWeather WHERE name = :name")
    fun geCityByName(name: String): Flow<CityWeather>

    @Query("SELECT * FROM CityWeather")
    fun getAll(): Flow<List<CityWeather>>

    @Update
    suspend fun update(city: CityWeather)

}