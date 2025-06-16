package com.martorell.albert.meteomartocompose.data.cityweather

import com.martorell.albert.meteomartocompose.data.auth.sources.cityweather.LocationLocalDataSource
import com.martorell.albert.meteomartocompose.framework.db.MeteoMartoDatabase
import com.martorell.albert.meteomartocompose.framework.db.model.LocationLocal

class LocationRoomDataImpl(db: MeteoMartoDatabase) : LocationLocalDataSource {

    private val locationDao = db.locationDao()

    override suspend fun saveLocation(
        latitude: Double?,
        longitude: Double?
    ) {

        locationDao.insertLocation(
            LocationLocal(
                latitude = latitude,
                longitude = longitude
            )
        )
    }

}