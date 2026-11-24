package com.martorell.albert.meteomartocompose.domain.model

/**
 * Discriminated union representing either a successful outcome [Success] or a typed
 * failure [Error] constrained to [DomainError].
 *
 * @param T Type of the value carried by [Success].
 * @param E Type of the [DomainError] carried by [Error].
 */
sealed interface Result<out T, out E : DomainError> {
    /**
     * Successful execution holding [data].
     *
     * @property data Produced domain value.
     */
    data class Success<out T>(val data: T) : Result<T, Nothing>

    /**
     * Failed execution holding a typed [error].
     *
     * @property error Typed [DomainError] describing the failure.
     */
    data class Error<out E : DomainError>(val error: E) : Result<Nothing, E>
}

/**
 * Transforms the value of a [Result.Success] using [transform] while propagating
 * any [Result.Error] unchanged.
 */
inline fun <T, E : DomainError, R> Result<T, E>.map(transform: (T) -> R): Result<R, E> =
    when (this) {
        is Result.Success -> Result.Success(transform(data))
        is Result.Error -> Result.Error(error)
    }

/**
 * Invokes [action] if this result is [Result.Success] and returns the original [Result]
 * for fluent chaining.
 */
inline fun <T, E : DomainError> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    if (this is Result.Success) action(data)
    return this
}

/**
 * Invokes [action] if this result is [Result.Error] and returns the original [Result]
 * for fluent chaining.
 */
inline fun <T, E : DomainError> Result<T, E>.onError(action: (E) -> Unit): Result<T, E> {
    if (this is Result.Error) action(error)
    return this
}

/**
 * Returns the encapsulated value if this result is [Result.Success], or `null` if it is [Result.Error].
 */
fun <T, E : DomainError> Result<T, E>.getOrNull(): T? =
    when (this) {
        is Result.Success -> data
        is Result.Error -> null
    }
