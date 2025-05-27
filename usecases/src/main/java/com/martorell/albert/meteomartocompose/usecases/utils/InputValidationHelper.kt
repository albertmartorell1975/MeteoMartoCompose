package com.martorell.albert.meteomartocompose.usecases.utils

class InputValidationHelper {

    fun credentialsReadyToBeSent(user: String, password: String): Boolean {

        return user.isFieldNotEmpty() && password.isFieldNotEmpty()

    }

}