package com.martorell.albert.meteomartocompose.usecases.cityweather

import com.martorell.albert.meteomartocompose.data.city.sources.SystemService
import javax.inject.Inject

/**
 * Use case to open the location source settings.
 * Following Clean Architecture: one business action per class.
 */
class OpenLocationSettingsUseCase @Inject constructor(
    private val systemService: SystemService,
) {
    operator fun invoke() = systemService.openLocationSettings()
}
