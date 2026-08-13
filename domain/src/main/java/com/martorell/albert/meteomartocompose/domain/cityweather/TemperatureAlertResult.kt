package com.martorell.albert.meteomartocompose.domain.cityweather

data class TemperatureAlertResult(
    val showAlert: Boolean,
    val currentTemperature: Double,
    val threshold: Double
)
