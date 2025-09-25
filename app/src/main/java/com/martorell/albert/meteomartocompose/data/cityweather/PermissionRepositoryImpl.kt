package com.martorell.albert.meteomartocompose.data.cityweather

import com.martorell.albert.meteomartocompose.data.city.repositories.PermissionChecker
import com.martorell.albert.meteomartocompose.data.city.repositories.PermissionRepository

class PermissionRepositoryImpl(private val permissionChecker: PermissionChecker) :
    PermissionRepository {

    override suspend fun checkLocationPermissions(): Boolean {

        val resultPermissions =
            permissionChecker.check(PermissionChecker.Permission.FINE_LOCATION) && (permissionChecker.check(
                PermissionChecker.Permission.COARSE_LOCATION
            ))

        return resultPermissions

    }

    override suspend fun isGPSEnabled(): Boolean = permissionChecker.isGPSEnabled()

}