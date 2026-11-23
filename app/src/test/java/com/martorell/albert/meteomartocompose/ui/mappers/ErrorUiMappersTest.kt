package com.martorell.albert.meteomartocompose.ui.mappers

import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.domain.model.DomainError
import com.martorell.albert.meteomartocompose.ui.util.UiText
import org.junit.Assert.assertEquals
import org.junit.Test

class ErrorUiMappersTest {

    @Test
    fun `toUiText maps every DomainError subtype to its localized string resource`() {
        val expectedMappings = listOf(
            DomainError.Network.NoInternet to R.string.error_connectivity,
            DomainError.Network.Timeout to R.string.error_timeout,
            DomainError.Network.Unauthorized to R.string.error_unauthorized,
            DomainError.Network.Serialization to R.string.error_serialization,
            DomainError.Network.ServerError(500) to R.string.error_server,
            DomainError.Network.Unknown to R.string.error_unknown,
            DomainError.Auth.InvalidCredentials to R.string.error_invalid_credentials,
            DomainError.Auth.WeakPassword to R.string.error_weak_password,
            DomainError.Auth.EmailAlreadyInUse to R.string.error_email_already_in_use,
            DomainError.Auth.UserNotFound to R.string.error_user_not_found,
            DomainError.Auth.SessionExpired to R.string.error_session_expired,
            DomainError.Auth.Unknown to R.string.error_auth_unknown,
            DomainError.Location.PermissionDenied to R.string.error_location_permission_denied,
            DomainError.Location.Disabled to R.string.error_location_disabled,
            DomainError.Location.Unavailable to R.string.error_location_unavailable,
            DomainError.Local.NotFound to R.string.error_city_not_found,
            DomainError.Local.DatabaseError to R.string.error_database,
            DomainError.Unknown to R.string.error_unknown,
        )

        expectedMappings.forEach { (error, expectedResId) ->
            assertEquals(UiText.StringResource(expectedResId), error.toUiText())
        }
    }
}
