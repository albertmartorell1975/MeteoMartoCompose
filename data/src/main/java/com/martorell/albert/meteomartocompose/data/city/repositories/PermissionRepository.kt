package com.martorell.albert.meteomartocompose.data.city.repositories

interface PermissionRepository {

    suspend fun checkLocationPermissions(): Boolean
    suspend fun isGPSEnabled(): Boolean

}