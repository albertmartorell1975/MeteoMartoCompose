package com.martorell.albert.meteomartocompose.data.city.sources

/**
 * Interface to abstract system-level actions from the domain and UI.
 */
interface SystemService {
    /**
     * Opens the application details settings screen.
     */
    fun openAppSettings()

    /**
     * Opens the location source settings screen.
     */
    fun openLocationSettings()
}