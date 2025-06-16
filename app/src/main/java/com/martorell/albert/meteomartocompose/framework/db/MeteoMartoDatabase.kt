package com.martorell.albert.meteomartocompose.framework.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.martorell.albert.meteomartocompose.framework.db.dao.CityDao
import com.martorell.albert.meteomartocompose.framework.db.dao.LocationDao
import com.martorell.albert.meteomartocompose.framework.db.dao.UserDao
import com.martorell.albert.meteomartocompose.framework.db.model.CityWeather
import com.martorell.albert.meteomartocompose.framework.db.model.LocationLocal
import com.martorell.albert.meteomartocompose.framework.db.model.User

@Database(entities = [User::class, CityWeather::class, LocationLocal::class], version = 4, exportSchema = false)
abstract class MeteoMartoDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun cityDao(): CityDao
    abstract fun locationDao(): LocationDao

}