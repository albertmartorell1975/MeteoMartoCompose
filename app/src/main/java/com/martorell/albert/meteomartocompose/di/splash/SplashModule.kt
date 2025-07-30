package com.martorell.albert.meteomartocompose.di.splash

import com.martorell.albert.meteomartocompose.usecases.splash.SplashInteractors
import com.martorell.albert.meteomartocompose.usecases.splash.UserLoggedUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class SplashModule {

    @Provides
    fun splashInteractorsProvider(
        userLoggedUseCase: UserLoggedUseCase
    ) = SplashInteractors(
        userLoggedUseCase = userLoggedUseCase
    )

}