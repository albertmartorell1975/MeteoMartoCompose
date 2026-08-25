package com.martorell.albert.meteomartocompose.utils

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.martorell.albert.meteomartocompose.data.city.repositories.PermissionChecker
import javax.inject.Inject

/**
 * Class that allows checking if a permission is granted.
 * Centralizes API version checks to avoid lint warnings and logic leaks.
 */
class AndroidPermissionChecker @Inject constructor(private val application: Application) :
    PermissionChecker {

    override suspend fun check(permission: PermissionChecker.Permission): Boolean {
        // If the permission is for notifications, and we are below Android 13, it's granted by default.
        if (permission == PermissionChecker.Permission.POST_NOTIFICATIONS &&
            Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU
        ) {
            return true
        }

        val androidPermission = permission.toAndroidId() ?: return true

        return ContextCompat.checkSelfPermission(
            application,
            androidPermission
        ) == PackageManager.PERMISSION_GRANTED
    }

    override suspend fun isGPSEnabled(): Boolean {
        val locationManager =
            application.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    override fun getWeatherPermissions(): List<String> = buildList {
        add(AppConstants.PERMISSION_FINE_LOCATION)
        add(AppConstants.PERMISSION_COARSE_LOCATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(AppConstants.PERMISSION_POST_NOTIFICATIONS)
        }
    }
}

private fun PermissionChecker.Permission.toAndroidId(): String? = when (this) {
    PermissionChecker.Permission.COARSE_LOCATION -> AppConstants.PERMISSION_COARSE_LOCATION
    PermissionChecker.Permission.FINE_LOCATION -> AppConstants.PERMISSION_FINE_LOCATION
    PermissionChecker.Permission.POST_NOTIFICATIONS -> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            AppConstants.PERMISSION_POST_NOTIFICATIONS
        } else {
            null
        }
    }
}
