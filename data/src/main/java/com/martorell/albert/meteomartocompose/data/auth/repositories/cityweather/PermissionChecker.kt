package com.martorell.albert.meteomartocompose.data.auth.repositories.cityweather

interface PermissionChecker {

    enum class Permission { COARSE_LOCATION, FINE_LOCATION }

    suspend fun check(permission: Permission): Boolean
    suspend fun isGPSEnabled(): Boolean

}