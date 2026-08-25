package com.martorell.albert.meteomartocompose.data.notification

interface NotificationService {
    suspend fun showHighTemperatureNotification(temperature: Double)
}
