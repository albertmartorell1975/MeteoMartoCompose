package com.martorell.albert.meteomartocompose.data.error

import android.database.SQLException
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.remoteconfig.FirebaseRemoteConfigClientException
import com.google.firebase.remoteconfig.FirebaseRemoteConfigServerException
import com.martorell.albert.meteomartocompose.data.auth.mappers.AuthErrorMapper
import com.martorell.albert.meteomartocompose.domain.model.DomainError
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import java.io.IOException
import java.net.SocketTimeoutException

class AndroidAndAuthErrorMapperTest {

    private val androidErrorMapper = AndroidErrorMapper()
    private val authErrorMapper = AuthErrorMapper(androidErrorMapper)

    @Test
    fun `AuthErrorMapper maps FirebaseAuth exceptions error codes and network causes deterministically`() {
        assertEquals(
            DomainError.Auth.WeakPassword,
            authErrorMapper.map(
                FirebaseAuthWeakPasswordException("ERROR_WEAK_PASSWORD", "Weak password", "Too short")
            ),
        )
        assertEquals(
            DomainError.Auth.InvalidCredentials,
            authErrorMapper.map(
                FirebaseAuthInvalidCredentialsException("ERROR_WRONG_PASSWORD", "Wrong password")
            ),
        )
        assertEquals(
            DomainError.Auth.EmailAlreadyInUse,
            authErrorMapper.map(
                FirebaseAuthUserCollisionException("ERROR_EMAIL_ALREADY_IN_USE", "Email in use")
            ),
        )
        assertEquals(
            DomainError.Auth.UserNotFound,
            authErrorMapper.map(
                FirebaseAuthInvalidUserException("ERROR_USER_NOT_FOUND", "User not found")
            ),
        )
        assertEquals(
            DomainError.Auth.SessionExpired,
            authErrorMapper.map(
                FirebaseAuthException("ERROR_USER_TOKEN_EXPIRED", "Token expired")
            ),
        )
        assertEquals(
            DomainError.Network.NoInternet,
            authErrorMapper.map(
                FirebaseAuthException("ERROR_NETWORK_REQUEST_FAILED", "Network failure")
            ),
        )
        assertEquals(
            DomainError.Auth.Unknown,
            authErrorMapper.map(
                FirebaseAuthException("ERROR_OTHER", "Unexpected auth failure")
            ),
        )
        assertEquals(
            DomainError.Network.NoInternet,
            authErrorMapper.map(FirebaseNetworkException("No network")),
        )
        assertEquals(
            DomainError.Network.Timeout,
            authErrorMapper.map(RuntimeException("Wrapped", SocketTimeoutException("Timeout"))),
        )

        try {
            authErrorMapper.map(CancellationException("Cancelled"))
            fail("Expected CancellationException to be rethrown by AuthErrorMapper")
        } catch (_: CancellationException) {
            // Expected
        }
    }

    @Test
    fun `AndroidErrorMapper maps serialization remote config location and delegates JVM errors`() {
        assertEquals(
            DomainError.Local.DatabaseError,
            androidErrorMapper.map(SQLException("SQLite failure")),
        )
        assertEquals(
            DomainError.Network.Serialization,
            androidErrorMapper.map(SerializationException("Malformed JSON")),
        )
        assertEquals(
            DomainError.Network.ServerError(503),
            androidErrorMapper.map(FirebaseRemoteConfigServerException(503, "Service unavailable")),
        )
        assertEquals(
            DomainError.Network.Unauthorized,
            androidErrorMapper.map(FirebaseRemoteConfigServerException(403, "Forbidden")),
        )
        assertEquals(
            DomainError.Network.Unknown,
            androidErrorMapper.map(FirebaseRemoteConfigClientException("Client error")),
        )
        assertEquals(
            DomainError.Network.NoInternet,
            androidErrorMapper.map(
                FirebaseRemoteConfigClientException("Offline", IOException("No internet"))
            ),
        )
        assertEquals(
            DomainError.Location.Unavailable,
            androidErrorMapper.map(ApiException(Status(CommonStatusCodes.INTERNAL_ERROR))),
        )
        assertEquals(
            DomainError.Network.NoInternet,
            androidErrorMapper.map(FirebaseNetworkException("Offline")),
        )
        assertEquals(
            DomainError.Location.PermissionDenied,
            androidErrorMapper.map(SecurityException("Location permission missing")),
        )
        assertEquals(
            DomainError.Unknown,
            androidErrorMapper.map(IllegalStateException("Unexpected")),
        )

        try {
            androidErrorMapper.map(CancellationException("Cancelled"))
            fail("Expected CancellationException to be rethrown by AndroidErrorMapper")
        } catch (_: CancellationException) {
            // Expected
        }
    }
}
