package com.martorell.albert.meteomartocompose.framework.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.martorell.albert.meteomartocompose.framework.db.model.LocationLocal

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: LocationLocal)

}