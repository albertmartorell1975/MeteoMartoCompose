package com.martorell.albert.meteomartocompose.utils

import android.content.Context
import com.martorell.albert.meteomartocompose.MeteoMartoApp
import java.math.RoundingMode

val Context.MeteoMartoApp: MeteoMartoApp
    get() = applicationContext as MeteoMartoApp

fun Double.fahrenheitToCelsius(): Double {
    return (this - 32) * 5 / 9
}

fun Double.openWeatherConverter(): Double {
    return (this.div(10).toBigDecimal().setScale(
        1, RoundingMode.HALF_UP
    )).toDouble()
}