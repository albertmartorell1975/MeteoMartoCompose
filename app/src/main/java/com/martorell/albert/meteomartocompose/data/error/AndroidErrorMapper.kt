package com.martorell.albert.meteomartocompose.data.error

import android.database.SQLException
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.FirebaseRemoteConfigServerException
import com.martorell.albert.meteomartocompose.data.util.ErrorMapper
import com.martorell.albert.meteomartocompose.data.util.HTTP_FORBIDDEN
import com.martorell.albert.meteomartocompose.data.util.HTTP_UNAUTHORIZED
import com.martorell.albert.meteomartocompose.data.util.toDomainError
import com.martorell.albert.meteomartocompose.domain.model.DomainError
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import java.io.IOException
import java.io.InterruptedIOException
import java.net.SocketTimeoutException
import java.util.Collections
import java.util.IdentityHashMap
import javax.inject.Inject

/**
 * Android-layer [ErrorMapper] implementation that handles platform, Firebase, Play Services
 * Location, and kotlinx.serialization exceptions before delegating pure JVM/Retrofit exceptions
 * to [toDomainError].
 *
 * Rethrows [CancellationException] anywhere in the cause chain to preserve structured concurrency.
 */
class AndroidErrorMapper @Inject constructor() : ErrorMapper {

    /**
     * Maps [throwable] to a typed [DomainError] by inspecting its cycle-guarded cause chain.
     */
    override fun map(throwable: Throwable): DomainError {
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

        for (current in chain) {
            when (current) {
                is SQLException -> return DomainError.Local.DatabaseError
                is SerializationException -> return DomainError.Network.Serialization
                is FirebaseRemoteConfigServerException -> return when (current.httpStatusCode) {
                    HTTP_UNAUTHORIZED, HTTP_FORBIDDEN -> DomainError.Network.Unauthorized
                    else -> DomainError.Network.ServerError(current.httpStatusCode)
                }

                is FirebaseRemoteConfigException -> return DomainError.Network.Unknown
                is ResolvableApiException -> return DomainError.Location.Disabled
                is ApiException -> return DomainError.Location.Unavailable
            }
        }

        return throwable.toDomainError()
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
}
