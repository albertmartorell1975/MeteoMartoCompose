package com.martorell.albert.meteomartocompose.data.auth

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.martorell.albert.meteomartocompose.data.CustomError
import com.martorell.albert.meteomartocompose.data.toCustomError
import java.io.IOException

/**
 * Specialized mapper for Firebase Authentication exceptions.
 * It centralizes the logic to differentiate between connectivity,
 * credential, and server-side errors.
 */
object AuthErrorMapper {

    private const val ERROR_NETWORK_REQUEST_FAILED = "ERROR_NETWORK_REQUEST_FAILED"

    fun map(ex: Exception): CustomError {
        return when (ex) {
            is FirebaseAuthInvalidCredentialsException,
            is FirebaseAuthInvalidUserException -> CustomError.InvalidCredentials

            is FirebaseNetworkException -> CustomError.Connectivity

            is FirebaseAuthException -> {
                if (ex.errorCode == ERROR_NETWORK_REQUEST_FAILED) {
                    CustomError.Connectivity
                } else {
                    // Fallback to standard Firebase mapping if it existed, 
                    // or treat as server error
                    CustomError.FirebaseError(0) // We could map more codes here if needed
                }
            }

            is IOException -> CustomError.Connectivity

            else -> {
                // If it contains "network" in the message or cause, it's likely a connectivity issue
                // wrapped in a generic Exception.
                val isNetworkRelated = ex.message?.contains("network", ignoreCase = true) == true ||
                        ex.cause?.message?.contains("network", ignoreCase = true) == true
                
                if (isNetworkRelated) {
                    CustomError.Connectivity
                } else {
                    ex.toCustomError()
                }
            }
        }
    }
}
