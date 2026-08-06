package com.martorell.albert.meteomartocompose.di

import com.martorell.albert.meteomartocompose.data.notification.NotificationService
import com.martorell.albert.meteomartocompose.framework.notification.NotificationServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationBindingModule {

    @Binds
    @Singleton
    abstract fun bindNotificationService(
        notificationServiceImpl: NotificationServiceImpl
    ): NotificationService
}
