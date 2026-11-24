package com.martorell.albert.meteomartocompose.data.util

import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.martorell.albert.meteomartocompose.domain.model.DomainError
import com.martorell.albert.meteomartocompose.domain.model.Result
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.io.InterruptedIOException
import java.net.SocketTimeoutException
import java.sql.SQLException

class ExceptionMapperTest {

    @Test
    fun `toDomainError maps network http serialization security sql and unknown throwables deterministically`() {
        assertEquals(DomainError.Network.Timeout, SocketTimeoutException("timeout").toDomainError())
        assertEquals(DomainError.Network.Timeout, InterruptedIOException("interrupted").toDomainError())
        assertEquals(DomainError.Network.NoInternet, IOException("offline").toDomainError())
        assertEquals(DomainError.Network.Unauthorized, httpException(401).toDomainError())
        assertEquals(DomainError.Network.Unauthorized, httpException(403).toDomainError())
        assertEquals(DomainError.Network.ServerError(500), httpException(500).toDomainError())
        assertEquals(DomainError.Network.ServerError(404), httpException(404).toDomainError())
        assertEquals(DomainError.Network.Serialization, JsonParseException("bad").toDomainError())
        assertEquals(DomainError.Network.Serialization, JsonSyntaxException("syntax").toDomainError())
        assertEquals(DomainError.Location.PermissionDenied, SecurityException("denied").toDomainError())
        assertEquals(DomainError.Local.DatabaseError, SQLException("db").toDomainError())
        assertEquals(DomainError.Unknown, IllegalStateException("unexpected").toDomainError())
        assertEquals(
            DomainError.Network.Timeout,
            RuntimeException("wrapped", SocketTimeoutException("nested")).toDomainError(),
        )

        val cyclicThrowable = object : RuntimeException("cyclic") {
            override val cause: Throwable get() = this
        }
        assertEquals(DomainError.Unknown, cyclicThrowable.toDomainError())
    }

    @Test
    fun `safeCall wraps success and maps non-cancellation exceptions to Result Error`() = runTest {
        assertEquals(Result.Success(42), safeCall { 42 })
        assertEquals(
            Result.Error(DomainError.Network.NoInternet),
            safeCall<Unit> { throw IOException("no network") },
        )
        assertEquals(Result.Success("ok"), safeSyncCall { "ok" })
        assertEquals(
            Result.Error(DomainError.Location.PermissionDenied),
            safeSyncCall<Unit> { throw SecurityException("permission denied") },
        )
    }

    @Test
    fun `safeCall rethrows CancellationException to preserve structured concurrency`() = runTest {
        try {
            safeCall<Unit> { throw CancellationException("cancelled") }
            fail("Expected CancellationException to be rethrown by safeCall")
        } catch (_: CancellationException) {
            // Expected
        }

        try {
            safeSyncCall<Unit> { throw CancellationException("cancelled") }
            fail("Expected CancellationException to be rethrown by safeSyncCall")
        } catch (_: CancellationException) {
            // Expected
        }
    }

    private fun httpException(code: Int): HttpException =
        HttpException(Response.error<Any>(code, "".toResponseBody(null)))
}
