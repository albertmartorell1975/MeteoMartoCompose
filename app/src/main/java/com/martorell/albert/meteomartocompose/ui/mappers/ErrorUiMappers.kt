package com.martorell.albert.meteomartocompose.ui.mappers

import com.martorell.albert.meteomartocompose.R
import com.martorell.albert.meteomartocompose.domain.model.DomainError
import com.martorell.albert.meteomartocompose.ui.util.UiText

/**
 * Exhaustively maps every [DomainError] variant to a localized [UiText] resource for
 * presentation in Compose UI and Android contexts.
 */
fun DomainError.toUiText(): UiText = when (this) {
    DomainError.Network.NoInternet -> UiText.StringResource(R.string.error_connectivity)
    DomainError.Network.Timeout -> UiText.StringResource(R.string.error_timeout)
    DomainError.Network.Unauthorized -> UiText.StringResource(R.string.error_unauthorized)
    DomainError.Network.Serialization -> UiText.StringResource(R.string.error_serialization)
    is DomainError.Network.ServerError -> UiText.StringResource(R.string.error_server)
    DomainError.Network.Unknown -> UiText.StringResource(R.string.error_unknown)
    DomainError.Auth.InvalidCredentials -> UiText.StringResource(R.string.error_invalid_credentials)
    DomainError.Auth.WeakPassword -> UiText.StringResource(R.string.error_weak_password)
    DomainError.Auth.EmailAlreadyInUse -> UiText.StringResource(R.string.error_email_already_in_use)
    DomainError.Auth.UserNotFound -> UiText.StringResource(R.string.error_user_not_found)
    DomainError.Auth.SessionExpired -> UiText.StringResource(R.string.error_session_expired)
    DomainError.Auth.Unknown -> UiText.StringResource(R.string.error_auth_unknown)
    DomainError.Location.PermissionDenied ->
        UiText.StringResource(R.string.error_location_permission_denied)

    DomainError.Location.Disabled -> UiText.StringResource(R.string.error_location_disabled)
    DomainError.Location.Unavailable -> UiText.StringResource(R.string.error_location_unavailable)
    DomainError.Local.NotFound -> UiText.StringResource(R.string.error_city_not_found)
    DomainError.Local.DatabaseError -> UiText.StringResource(R.string.error_database)
    DomainError.Unknown -> UiText.StringResource(R.string.error_unknown)
}
