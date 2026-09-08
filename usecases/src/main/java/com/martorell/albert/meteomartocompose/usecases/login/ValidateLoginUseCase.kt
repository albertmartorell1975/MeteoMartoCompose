package com.martorell.albert.meteomartocompose.usecases.login

import com.martorell.albert.meteomartocompose.usecases.utils.ValidateEmailUseCase
import com.martorell.albert.meteomartocompose.usecases.utils.ValidatePasswordUseCase
import javax.inject.Inject

class ValidateLoginUseCase @Inject constructor(
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
) {

    operator fun invoke(
        email: String,
        password: String
    ): Boolean = validateEmailUseCase(email) && validatePasswordUseCase(password)

}