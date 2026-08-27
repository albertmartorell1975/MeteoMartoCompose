package com.martorell.albert.meteomartocompose

import android.app.Application

/**
 * A plain Application class used for Robolectric tests to avoid Hilt and Firebase initialization.
 * This prevents IllegalStateException during test environment setup.
 */
class TestMeteoMartoApp : Application()
