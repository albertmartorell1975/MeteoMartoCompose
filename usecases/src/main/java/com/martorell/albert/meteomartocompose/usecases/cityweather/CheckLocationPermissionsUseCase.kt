package com.martorell.albert.meteomartocompose.usecases.cityweather

import com.martorell.albert.meteomartocompose.data.auth.repositories.cityweather.PermissionRepository
import javax.inject.Inject

class CheckLocationPermissionsUseCase @Inject constructor(private val permissionRepository: PermissionRepository) {

    suspend fun invoke(
    ): Boolean =
        permissionRepository.checkLocationPermissions()

}