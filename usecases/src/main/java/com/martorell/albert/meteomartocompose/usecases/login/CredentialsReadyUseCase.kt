package com.martorell.albert.meteomartocompose.usecases.login

import com.martorell.albert.meteomartocompose.usecases.utils.InputValidationHelper
import javax.inject.Inject

class CredentialsReadyUseCase @Inject constructor(private val inputValdationHelper: InputValidationHelper) {

    fun invoke(
        user: String,
        password: String
    ): Boolean =
        inputValdationHelper.credentialsReadyToBeSent(user, password)

}