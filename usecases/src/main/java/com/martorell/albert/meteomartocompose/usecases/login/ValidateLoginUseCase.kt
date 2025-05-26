package com.martorell.albert.meteomartocompose.usecases.login

import javax.inject.Inject

class ValidateLoginUseCase @Inject constructor() {

    fun invoke(
        user: String,
        password: String
    ): Boolean =
        user.isNotEmpty() && password.length > 5

}