package com.martorell.albert.meteomartocompose.framework.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.martorell.albert.meteomartocompose.framework.db.dao.UserDao
import com.martorell.albert.meteomartocompose.framework.db.model.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class MeteoMartoDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

}