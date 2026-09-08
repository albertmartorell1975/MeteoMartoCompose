package com.martorell.albert.meteomartocompose.usecases.login

import com.martorell.albert.meteomartocompose.usecases.utils.ValidateEmailUseCase
import com.martorell.albert.meteomartocompose.usecases.utils.ValidatePasswordUseCase

data class LoginInteractors(
    val validateLoginUseCase: ValidateLoginUseCase,
    val validateEmailUseCase: ValidateEmailUseCase,
    val validatePasswordUseCase: ValidatePasswordUseCase,
    val logInUseCase: LogInUseCase
)
