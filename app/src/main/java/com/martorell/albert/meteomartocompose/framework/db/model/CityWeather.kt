package com.martorell.albert.meteomartocompose.framework.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CityWeather(
    @PrimaryKey(autoGenerate = false)
    val name: String = "",
    val weatherDescription: String? = "",
    val weatherIcon: String? = "",
    val temperature: Double,
    val temperatureMin: Double,
    val temperatureMax: Double,
    val rain: Double? = 0.0,
    val pressure: Int,
    val favorite: Boolean = false
)