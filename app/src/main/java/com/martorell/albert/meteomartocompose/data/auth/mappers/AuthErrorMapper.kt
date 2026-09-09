package com.martorell.albert.meteomartocompose.data.auth.mappers

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.martorell.albert.meteomartocompose.data.CustomError
import com.martorell.albert.meteomartocompose.data.toCustomError

/**
 * Specialized mapper for Firebase Authentication exceptions.
 * It centralizes the logic to differentiate between connectivity,
 * credential, and server-side errors.
 */
object AuthErrorMapper {

    private const val FIREBASE_ERROR_NETWORK = "ERROR_NETWORK_REQUEST_FAILED"

    fun map(throwable: Throwable): CustomError {
        if (throwable.isConnectivityError()) {
            return CustomError.Connectivity
        }

        return when (throwable) {
            is FirebaseAuthInvalidCredentialsException,
            is FirebaseAuthInvalidUserException,
                -> CustomError.InvalidCredentials

            is FirebaseAuthException -> {
                if (throwable.errorCode == FIREBASE_ERROR_NETWORK) {
                    CustomError.Connectivity
                } else {
                    CustomError.FirebaseError(0)
                }
            }

            is Exception -> throwable.toCustomError()

            else -> CustomError.Unknown(throwable.message ?: "Unknown error")
        }
    }

    /**
     * Recursively checks if the throwable or any of its causes are network-related.
     */
    private fun Throwable.isConnectivityError(): Boolean {
        var current: Throwable? = this
        while (current != null) {
            if (current is java.io.IOException || current is FirebaseNetworkException) {
                return true
            }
            current = current.cause
        }
        return false
    }
}
