package com.martorell.albert.meteomartocompose.domain.model

/**
 * Pure, framework-independent hierarchy of typed domain failures across the application.
 *
 * Subtypes intentionally omit raw [Throwable] references or exception messages so
 * presentation layers resolve errors deterministically to localized user-facing copy.
 */
sealed interface DomainError {

    /**
     * Failures originating from remote API calls, connectivity, or payload serialization.
     */
    sealed interface Network : DomainError {
        /** Device has no active network connection or a transport-level I/O failure occurred. */
        data object NoInternet : Network

        /** Remote request exceeded the allowed socket or I/O timeout window. */
        data object Timeout : Network

        /** Remote endpoint rejected the request with an unauthorized or forbidden status (401/403). */
        data object Unauthorized : Network

        /** Response or configuration payload could not be parsed into the expected schema. */
        data object Serialization : Network

        /**
         * Remote server returned a non-2xx HTTP status other than unauthorized/forbidden.
         *
         * @property code HTTP status code returned by the server.
         */
        data class ServerError(val code: Int) : Network

        /** Unclassified network or remote configuration failure. */
        data object Unknown : Network
    }

    /**
     * Failures originating from user authentication or session validation.
     */
    sealed interface Auth : DomainError {
        /** Provided email or password credentials were rejected. */
        data object InvalidCredentials : Auth

        /** Chosen password does not meet the minimum strength requirements. */
        data object WeakPassword : Auth

        /** Registration failed because an account already exists for the email address. */
        data object EmailAlreadyInUse : Auth

        /** No user account matches the supplied identifier. */
        data object UserNotFound : Auth

        /** Authenticated session or token has expired and requires re-authentication. */
        data object SessionExpired : Auth

        /** Unclassified authentication failure. */
        data object Unknown : Auth
    }

    /**
     * Failures originating from device location permissions or location provider state.
     */
    sealed interface Location : DomainError {
        /** Required location permission has not been granted by the user. */
        data object PermissionDenied : Location

        /** Device location services are turned off or require user resolution. */
        data object Disabled : Location

        /** Location provider failed or returned no usable coordinates. */
        data object Unavailable : Location
    }

    /**
     * Failures originating from local persistence or cache lookups.
     */
    sealed interface Local : DomainError {
        /** Requested record does not exist in local storage. */
        data object NotFound : Local

        /** Local database read or write operation failed. */
        data object DatabaseError : Local
    }

    /** Fallback failure when an exception does not match any known domain category. */
    data object Unknown : DomainError
}
