package com.martorell.albert.meteomartocompose.utils

/**
 * Application logging contract that decouples presentation and framework components
 * from `android.util.Log` so callers remain testable on the pure JVM.
 */
interface AppLogger {

    /**
     * Emits a debug-level diagnostic message under [tag].
     */
    fun debug(tag: String, message: String)

    /**
     * Emits an error-level message under [tag], optionally attaching an associated [throwable].
     */
    fun error(tag: String, message: String, throwable: Throwable? = null)
}
