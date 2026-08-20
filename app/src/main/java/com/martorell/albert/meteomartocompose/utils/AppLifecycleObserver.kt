package com.martorell.albert.meteomartocompose.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import javax.inject.Inject
import javax.inject.Singleton

interface AppLifecycleObserver {
    fun isAppInForeground(): Boolean
}

@Singleton
class AppLifecycleObserverImpl @Inject constructor() : AppLifecycleObserver {
    override fun isAppInForeground(): Boolean {
        return ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)
    }
}
