package com.martorell.albert.meteomartocompose.usecases.utils

import javax.inject.Inject

class ValidatePasswordUseCase @Inject constructor() {
    operator fun invoke(password: String): Boolean {
        return password.length >= Constants.MINIMUM_PASSWORD_LENGTH
    }
}
