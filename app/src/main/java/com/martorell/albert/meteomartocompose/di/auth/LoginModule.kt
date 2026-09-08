package com.martorell.albert.meteomartocompose.di.auth

import com.martorell.albert.meteomartocompose.usecases.login.LogInUseCase
import com.martorell.albert.meteomartocompose.usecases.login.LoginInteractors
import com.martorell.albert.meteomartocompose.usecases.login.ValidateLoginUseCase
import com.martorell.albert.meteomartocompose.usecases.utils.ValidateEmailUseCase
import com.martorell.albert.meteomartocompose.usecases.utils.ValidatePasswordUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * This module contains references related to the Data layer
 * This module is linked to a default component, in this case ActivityRetainedComponent.
 * So this module will live the same time that the component (ViewModel)
 */

@Module
@InstallIn(ViewModelComponent::class)
class LoginModule {

    @Provides
    fun loginInteractorsProvider(
        validateLoginUseCase: ValidateLoginUseCase,
        validateEmailUseCase: ValidateEmailUseCase,
        validatePasswordUseCase: ValidatePasswordUseCase,
        logInUseCase: LogInUseCase
    ) = LoginInteractors(
        validateLoginUseCase = validateLoginUseCase,
        validateEmailUseCase = validateEmailUseCase,
        validatePasswordUseCase = validatePasswordUseCase,
        logInUseCase = logInUseCase
    )

}
