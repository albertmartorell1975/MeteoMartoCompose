package com.martorell.albert.meteomartocompose.usecases.utils

class InputValidationHelper {

    fun credentialsReadyToBeSent(email: String, password: String): Boolean {

        return email.isFieldNotEmpty() && password.isFieldNotEmpty()

    }

}