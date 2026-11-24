package com.martorell.albert.meteomartocompose.data.util

import com.martorell.albert.meteomartocompose.domain.model.DomainError

/**
 * Translates technical [Throwable] instances caught at data-source boundaries into typed
 * [DomainError] values.
 *
 * Implementations must rethrow `CancellationException` rather than mapping it so structured
 * coroutine cancellation is preserved.
 */
interface ErrorMapper {
    /**
     * Maps [throwable] (and its wrapped cause chain where applicable) to a [DomainError].
     */
    fun map(throwable: Throwable): DomainError
}
