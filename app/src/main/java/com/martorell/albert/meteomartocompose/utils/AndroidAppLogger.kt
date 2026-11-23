package com.martorell.albert.meteomartocompose.utils

import android.util.Log
import javax.inject.Inject

/**
 * Production [AppLogger] implementation backed by Android's [android.util.Log].
 */
class AndroidAppLogger @Inject constructor() : AppLogger {

    /**
     * Logs a debug message to [Log.d].
     */
    override fun debug(tag: String, message: String) {
        Log.d(tag, message)
    }

    /**
     * Logs an error message to [Log.e], forwarding [throwable] only when non-null.
     */
    override fun error(tag: String, message: String, throwable: Throwable?) {
        if (throwable != null) {
            Log.e(tag, message, throwable)
        } else {
            Log.e(tag, message)
        }
    }
}
