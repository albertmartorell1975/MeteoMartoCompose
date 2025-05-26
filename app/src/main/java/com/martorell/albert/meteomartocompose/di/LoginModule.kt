package com.martorell.albert.meteomartocompose.di

import com.martorell.albert.meteomartocompose.usecases.login.LoginInteractors
import com.martorell.albert.meteomartocompose.usecases.login.ValidateLoginUseCase
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
        validateLoginUseCase: ValidateLoginUseCase
    ) = LoginInteractors(validateLoginUseCase)

    /* @Provides
     fun forgotPasswordServerSourceProvider(
         forgotPasswordApi: ForgotPasswordApi,
         headerForgotPasswordParams: HeaderForgotPasswordParams
     ): ForgotPasswordServerSource =
         ImpForgotPasswordServerSource(
             forgotPasswordApi,
             headerForgotPasswordParams
         )

     @Provides
     fun forgotPwMethodInteractorProvider(
         smsActivationStatus: GetSMSActivationStatus
     ) = InteractorForgotPWMethod(
         smsActivationStatus
     )

     */

}