package com.martorell.albert.meteomartocompose.utils

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
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

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavController, navGraphRoute: String, navBackStackEntry: NavBackStackEntry
): T {
    val parentEntry  = remember(navBackStackEntry){
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}