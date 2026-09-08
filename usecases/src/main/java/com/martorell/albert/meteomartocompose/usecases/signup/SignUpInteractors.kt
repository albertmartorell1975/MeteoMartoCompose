package com.martorell.albert.meteomartocompose.usecases.signup

import com.martorell.albert.meteomartocompose.usecases.utils.ValidateEmailUseCase
import com.martorell.albert.meteomartocompose.usecases.utils.ValidatePasswordUseCase

data class SignUpInteractors(
    val validateEmailUseCase: ValidateEmailUseCase,
    val validatePasswordUseCase: ValidatePasswordUseCase,
    val signUpUseCase: SignUpUseCase,
)
