package com.martorell.albert.meteomartocompose.utils

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.martorell.albert.meteomartocompose.data.auth.repositories.cityweather.PermissionChecker

/**
 * Entity that allow us checking if a permission is granted
 */

class AndroidPermissionChecker(private val application: Application) :
    PermissionChecker {

    override suspend fun check(permission: PermissionChecker.Permission): Boolean =
        ContextCompat.checkSelfPermission(
            application,
            permission.toAndroidId()
        ) == PackageManager.PERMISSION_GRANTED


    override suspend fun isGPSEnabled(): Boolean {

        val locationManager =
            application.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

    }

}

private fun PermissionChecker.Permission.toAndroidId() = when (this) {

    PermissionChecker.Permission.COARSE_LOCATION -> ACCESS_COARSE_LOCATION
    PermissionChecker.Permission.FINE_LOCATION -> ACCESS_FINE_LOCATION

}