package com.martorell.albert.meteomartocompose.data.cityweather

import com.martorell.albert.meteomartocompose.data.city.sources.LocationLocalDataSource
import com.martorell.albert.meteomartocompose.framework.db.MeteoMartoDatabase
import com.martorell.albert.meteomartocompose.framework.db.model.LocationLocal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocationRoomDataImpl(db: MeteoMartoDatabase) : LocationLocalDataSource {

    private val locationDao = db.locationDao()

    override suspend fun saveLocation(
        latitude: Double?,
        longitude: Double?
    ) {

        withContext(Dispatchers.IO) {
            locationDao.cleanTable()
            locationDao.insertLocation(
                LocationLocal(
                    latitude = latitude,
                    longitude = longitude
                )
            )
        }

    }

}