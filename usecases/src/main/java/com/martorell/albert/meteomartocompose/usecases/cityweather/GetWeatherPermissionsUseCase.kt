package com.martorell.albert.meteomartocompose.usecases.cityweather

import com.martorell.albert.meteomartocompose.data.city.repositories.PermissionRepository
import javax.inject.Inject

class GetWeatherPermissionsUseCase @Inject constructor(private val permissionRepository: PermissionRepository) {

    operator fun invoke(): List<String> =
        permissionRepository.getWeatherPermissions()

}
