package com.martorell.albert.meteomartocompose.data.auth.repositories.cityweather

interface PermissionRepository {

    suspend fun checkLocationPermissions(): Boolean
    suspend fun isGPSEnabled(): Boolean

}