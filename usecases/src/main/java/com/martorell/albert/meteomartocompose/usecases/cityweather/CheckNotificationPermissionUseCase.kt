package com.martorell.albert.meteomartocompose.usecases.cityweather

import com.martorell.albert.meteomartocompose.data.auth.repositories.cityweather.PermissionRepository
import javax.inject.Inject

class CheckNotificationPermissionUseCase @Inject constructor(private val permissionRepository: PermissionRepository) {

    suspend operator fun invoke(): Boolean =
        permissionRepository.checkNotificationPermission()

}