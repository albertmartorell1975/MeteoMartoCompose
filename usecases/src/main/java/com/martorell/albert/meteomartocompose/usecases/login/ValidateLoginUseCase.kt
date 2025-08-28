package com.martorell.albert.meteomartocompose.usecases.login

import com.martorell.albert.meteomartocompose.usecases.utils.InputValidationHelper
import javax.inject.Inject

class ValidateLoginUseCase @Inject constructor(private val inputValdationHelper: InputValidationHelper) {

    fun invoke(
        email: String,
        password: String
    ): Boolean =
        inputValdationHelper.credentialsReadyToBeSent(
            email = email,
            password = password
        )

}