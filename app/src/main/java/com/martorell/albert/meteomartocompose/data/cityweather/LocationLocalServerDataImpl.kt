package com.martorell.albert.meteomartocompose.data.cityweather

import androidx.room.withTransaction
import com.martorell.albert.meteomartocompose.data.city.sources.LocationLocalDataSource
import com.martorell.albert.meteomartocompose.framework.db.MeteoMartoDatabase
import com.martorell.albert.meteomartocompose.framework.db.model.LocationLocal
import javax.inject.Inject

class LocationRoomDataImpl @Inject constructor(
    private val db: MeteoMartoDatabase,
) : LocationLocalDataSource {

    private val locationDao = db.locationDao()

    override suspend fun saveLocation(
        latitude: Double?,
        longitude: Double?,
    ) {
        db.withTransaction {
            locationDao.cleanTable()
            locationDao.insertLocation(
                LocationLocal(
                    latitude = latitude,
                    longitude = longitude,
                ),
            )
        }
    }
}
