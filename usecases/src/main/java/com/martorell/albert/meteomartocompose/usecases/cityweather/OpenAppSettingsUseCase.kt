package com.martorell.albert.meteomartocompose.usecases.cityweather

import com.martorell.albert.meteomartocompose.data.city.sources.SystemService
import javax.inject.Inject

/**
 * Use case to open the application settings.
 * Following Clean Architecture: one business action per class.
 */
class OpenAppSettingsUseCase @Inject constructor(
    private val systemService: SystemService,
) {
    operator fun invoke() = systemService.openAppSettings()
}
