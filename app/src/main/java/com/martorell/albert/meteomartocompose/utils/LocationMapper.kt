package com.martorell.albert.meteomartocompose.utils

import android.location.Location
import com.martorell.albert.meteomartocompose.domain.cityweather.CurrentLocationDomain

fun Location?.toDomain(): CurrentLocationDomain =
    CurrentLocationDomain(
        longitude = this?.longitude,
        latitude = this?.latitude
    )