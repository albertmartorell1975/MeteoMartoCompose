package com.martorell.albert.meteomartocompose.data.cityweather

import com.martorell.albert.meteomartocompose.data.city.repositories.PermissionChecker
import com.martorell.albert.meteomartocompose.data.city.repositories.PermissionRepository
import javax.inject.Inject

class PermissionRepositoryImpl @Inject constructor(private val permissionChecker: PermissionChecker) :
    PermissionRepository {

    override suspend fun checkLocationPermissions(): Boolean {

        val resultPermissions =
            permissionChecker.check(PermissionChecker.Permission.FINE_LOCATION) && (permissionChecker.check(
                PermissionChecker.Permission.COARSE_LOCATION
            ))

        return resultPermissions

    }

    override suspend fun checkNotificationPermission(): Boolean =
        permissionChecker.check(PermissionChecker.Permission.POST_NOTIFICATIONS)

    override suspend fun isGPSEnabled(): Boolean = permissionChecker.isGPSEnabled()

    override fun getWeatherPermissions(): List<String> = permissionChecker.getWeatherPermissions()

}