package com.martorell.albert.meteomartocompose.framework.system

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.martorell.albert.meteomartocompose.data.city.sources.SystemService
import com.martorell.albert.meteomartocompose.utils.AppConstants
import javax.inject.Inject

/**
 * Android implementation of [SystemService].
 * Following the Zero System Leaks mandate by encapsulating Android-specific APIs.
 */
class AndroidSystemService @Inject constructor(
    private val application: Application
) : SystemService {

    override fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts(AppConstants.SCHEME_PACKAGE, application.packageName, null)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        application.startActivity(intent)
    }

    override fun openLocationSettings() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        application.startActivity(intent)
    }
}
