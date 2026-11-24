package com.martorell.albert.meteomartocompose.data.util

import com.google.gson.JsonParseException
import com.martorell.albert.meteomartocompose.domain.model.DomainError
import com.martorell.albert.meteomartocompose.domain.model.Result
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException
import java.io.InterruptedIOException
import java.net.SocketTimeoutException
import java.sql.SQLException
import java.util.Collections
import java.util.IdentityHashMap

/** HTTP status code indicating missing or invalid authentication credentials, mapped to [DomainError.Network.Unauthorized]. */
const val HTTP_UNAUTHORIZED = 401

/** HTTP status code indicating an authenticated request lacks permission, mapped to [DomainError.Network.Unauthorized]. */
const val HTTP_FORBIDDEN = 403

/**
 * Maps pure JVM, Retrofit, serialization, permission, and SQL [Throwable] chains to a typed
 * [DomainError], guarding against cyclic `cause` references.
 *
 * Rethrows [CancellationException] immediately if encountered anywhere in the cause chain to
 * preserve structured concurrency.
 */
fun Throwable.toDomainError(): DomainError {
    val visited = Collections.newSetFromMap(IdentityHashMap<Throwable, Boolean>())
    var current: Throwable? = this
    while (current != null && visited.add(current)) {
        when (current) {
            is CancellationException -> throw current
            is SocketTimeoutException,
            is InterruptedIOException,
            -> return DomainError.Network.Timeout

            is IOException -> return DomainError.Network.NoInternet
            is HttpException -> return when (current.code()) {
                HTTP_UNAUTHORIZED, HTTP_FORBIDDEN -> DomainError.Network.Unauthorized
                else -> DomainError.Network.ServerError(current.code())
            }

            is JsonParseException -> return DomainError.Network.Serialization
            is SecurityException -> return DomainError.Location.PermissionDenied
            is SQLException -> return DomainError.Local.DatabaseError
        }
        current = current.cause
    }
    return DomainError.Unknown
}

/**
 * Executes a suspending [action] and wraps its outcome in a [Result], translating any caught
 * [Exception] via [mapper] while rethrowing [CancellationException] untouched.
 *
 * @param mapper Translates non-cancellation exceptions into a [DomainError].
 * @param action Suspending operation to execute.
 * @return [Result.Success] with the returned value, or [Result.Error] with the mapped [DomainError].
 */
suspend inline fun <T> safeCall(
    mapper: (Throwable) -> DomainError = { it.toDomainError() },
    action: suspend () -> T,
): Result<T, DomainError> = try {
    Result.Success(action())
} catch (e: CancellationException) {
    throw e
} catch (e: Exception) {
    Result.Error(mapper(e))
}

/**
 * Executes a synchronous [action] and wraps its outcome in a [Result], translating any caught
 * [Exception] via [mapper] while rethrowing [CancellationException] untouched.
 *
 * @param mapper Translates non-cancellation exceptions into a [DomainError].
 * @param action Synchronous operation to execute.
 * @return [Result.Success] with the returned value, or [Result.Error] with the mapped [DomainError].
 */
inline fun <T> safeSyncCall(
    mapper: (Throwable) -> DomainError = { it.toDomainError() },
    action: () -> T,
): Result<T, DomainError> = try {
    Result.Success(action())
} catch (e: CancellationException) {
    throw e
} catch (e: Exception) {
    Result.Error(mapper(e))
}
