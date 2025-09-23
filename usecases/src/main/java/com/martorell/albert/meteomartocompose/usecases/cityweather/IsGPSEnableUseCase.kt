package com.martorell.albert.meteomartocompose.usecases.cityweather

import com.martorell.albert.meteomartocompose.data.city.repositories.PermissionRepository
import javax.inject.Inject

class IsGPSEnableUseCase @Inject constructor(private val permissionRepository: PermissionRepository) {

    suspend fun invoke(
    ): Boolean =
        permissionRepository.isGPSEnabled()

}