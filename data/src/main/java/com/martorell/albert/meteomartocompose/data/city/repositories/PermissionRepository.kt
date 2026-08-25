package com.martorell.albert.meteomartocompose.data.city.repositories

interface PermissionRepository {

    suspend fun checkLocationPermissions(): Boolean
    suspend fun checkNotificationPermission(): Boolean
    suspend fun isGPSEnabled(): Boolean
    fun getWeatherPermissions(): List<String>

}