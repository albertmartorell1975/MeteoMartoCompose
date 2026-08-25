package com.martorell.albert.meteomartocompose.utils

/**
 * Global constants for the application to avoid magic strings and literals.
 * Following kotlin-style and android-intent-security skills.
 */
object AppConstants {
    /**
     * URI scheme used to open the application details settings.
     */
    const val SCHEME_PACKAGE = "package"

    /**
     * Internal deep link base URI.
     */
    const val DEEP_LINK_SCHEME = "meteomarto"
    const val DEEP_LINK_HOST_ALERT = "alert"
    const val DEEP_LINK_PATH_HIGH_TEMP = "high-temperature"
    const val DEEP_LINK_BASE = "$DEEP_LINK_SCHEME://$DEEP_LINK_HOST_ALERT/$DEEP_LINK_PATH_HIGH_TEMP"

    /**
     * Notification related constants.
     */
    const val NOTIFICATION_CHANNEL_ID = "high_temp_channel"
    const val NOTIFICATION_ID = 1001

    /**
     * Android System Permissions.
     * Centralized to avoid magic strings and ensure consistency between Checker and UI.
     */
    const val PERMISSION_FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION
    const val PERMISSION_COARSE_LOCATION = android.Manifest.permission.ACCESS_COARSE_LOCATION
    const val PERMISSION_POST_NOTIFICATIONS = android.Manifest.permission.POST_NOTIFICATIONS // Available from API 33

    /**
     * Logging tags.
     */
    const val DEBUG_TAG = "MeteoMartoDebug"
}
