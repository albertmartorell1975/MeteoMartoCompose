package com.martorell.albert.meteomartocompose.usecases.cityweather

import com.martorell.albert.meteomartocompose.data.auth.repositories.cityweather.PermissionRepository
import javax.inject.Inject

class GetWeatherPermissionsUseCase @Inject constructor(private val permissionRepository: PermissionRepository) {

    operator fun invoke(): List<String> =
        permissionRepository.getWeatherPermissions()

}
