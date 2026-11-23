package com.martorell.albert.meteomartocompose.data.auth.mappers

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.martorell.albert.meteomartocompose.data.util.ErrorMapper
import com.martorell.albert.meteomartocompose.domain.model.DomainError
import kotlinx.coroutines.CancellationException
import java.io.IOException
import java.io.InterruptedIOException
import java.net.SocketTimeoutException
import java.util.Collections
import java.util.IdentityHashMap
import javax.inject.Inject

/**
 * Maps Firebase Authentication exceptions and error codes into typed [DomainError.Auth] or
 * [DomainError.Network] failures, delegating unrelated throwables to [errorMapper].
 *
 * Preserves structured concurrency by rethrowing [CancellationException] found anywhere in the
 * cause chain.
 */
class AuthErrorMapper @Inject constructor(
    private val errorMapper: ErrorMapper,
) {

    /**
     * Translates [throwable] into a [DomainError], prioritizing coroutine cancellation and
     * connectivity causes before inspecting specific `FirebaseAuthException` subtypes and codes.
     */
    fun map(throwable: Throwable): DomainError {
        val chain = collectCauseChain(throwable)

        for (current in chain) {
            if (current is CancellationException) {
                throw current
            }
        }

        for (current in chain) {
            when (current) {
                is SocketTimeoutException,
                is InterruptedIOException,
                -> return DomainError.Network.Timeout

                is FirebaseNetworkException,
                is IOException,
                -> return DomainError.Network.NoInternet
            }
        }

        return when (throwable) {
            // FirebaseAuthWeakPasswordException extends FirebaseAuthInvalidCredentialsException
            is FirebaseAuthWeakPasswordException -> DomainError.Auth.WeakPassword
            is FirebaseAuthInvalidCredentialsException -> DomainError.Auth.InvalidCredentials
            is FirebaseAuthUserCollisionException -> DomainError.Auth.EmailAlreadyInUse
            is FirebaseAuthInvalidUserException -> when (throwable.errorCode) {
                ERROR_USER_TOKEN_EXPIRED,
                ERROR_INVALID_USER_TOKEN,
                -> DomainError.Auth.SessionExpired

                else -> DomainError.Auth.UserNotFound
            }

            is FirebaseAuthException -> mapFirebaseAuthErrorCode(throwable.errorCode)
            else -> errorMapper.map(throwable)
        }
    }

    private fun mapFirebaseAuthErrorCode(errorCode: String): DomainError = when (errorCode) {
        ERROR_NETWORK_REQUEST_FAILED -> DomainError.Network.NoInternet
        ERROR_WEAK_PASSWORD -> DomainError.Auth.WeakPassword
        ERROR_INVALID_CREDENTIAL,
        ERROR_WRONG_PASSWORD,
        ERROR_INVALID_EMAIL,
        -> DomainError.Auth.InvalidCredentials

        ERROR_EMAIL_ALREADY_IN_USE,
        ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL,
        -> DomainError.Auth.EmailAlreadyInUse

        ERROR_USER_NOT_FOUND -> DomainError.Auth.UserNotFound
        ERROR_USER_TOKEN_EXPIRED,
        ERROR_INVALID_USER_TOKEN,
        ERROR_REQUIRES_RECENT_LOGIN,
        -> DomainError.Auth.SessionExpired

        else -> DomainError.Auth.Unknown
    }

    private fun collectCauseChain(throwable: Throwable): List<Throwable> {
        val visited = Collections.newSetFromMap(IdentityHashMap<Throwable, Boolean>())
        val chain = mutableListOf<Throwable>()
        var current: Throwable? = throwable
        while (current != null && visited.add(current)) {
            chain += current
            current = current.cause
        }
        return chain
    }

    companion object {
        private const val ERROR_NETWORK_REQUEST_FAILED = "ERROR_NETWORK_REQUEST_FAILED"
        private const val ERROR_WEAK_PASSWORD = "ERROR_WEAK_PASSWORD"
        private const val ERROR_INVALID_CREDENTIAL = "ERROR_INVALID_CREDENTIAL"
        private const val ERROR_WRONG_PASSWORD = "ERROR_WRONG_PASSWORD"
        private const val ERROR_INVALID_EMAIL = "ERROR_INVALID_EMAIL"
        private const val ERROR_EMAIL_ALREADY_IN_USE = "ERROR_EMAIL_ALREADY_IN_USE"
        private const val ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL =
            "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL"
        private const val ERROR_USER_NOT_FOUND = "ERROR_USER_NOT_FOUND"
        private const val ERROR_USER_TOKEN_EXPIRED = "ERROR_USER_TOKEN_EXPIRED"
        private const val ERROR_INVALID_USER_TOKEN = "ERROR_INVALID_USER_TOKEN"
        private const val ERROR_REQUIRES_RECENT_LOGIN = "ERROR_REQUIRES_RECENT_LOGIN"
    }
}
