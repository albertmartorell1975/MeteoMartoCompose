package com.martorell.albert.meteomartocompose.usecases.utils

import javax.inject.Inject

class ValidateEmailUseCase @Inject constructor() {
    operator fun invoke(email: String): Boolean {
        return email.matches(Constants.EMAIL_REGEX.toRegex())
    }
}
