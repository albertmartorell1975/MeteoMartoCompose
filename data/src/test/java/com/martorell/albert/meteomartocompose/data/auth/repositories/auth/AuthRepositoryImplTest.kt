package com.martorell.albert.meteomartocompose.data.auth.repositories.auth

import com.martorell.albert.meteomartocompose.data.CustomError
import com.martorell.albert.meteomartocompose.data.auth.sources.auth.AccountService
import com.martorell.albert.meteomartocompose.data.auth.sources.auth.AuthLocalDataSource
import com.martorell.albert.meteomartocompose.domain.auth.UserDomain
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import arrow.core.left
import arrow.core.right

class AuthRepositoryImplTest {

    private val accountService: AccountService = mockk()
    private val authLocalDataSource: AuthLocalDataSource = mockk(relaxed = true)
    private val repository = AuthRepositoryImpl(accountService, authLocalDataSource)

    private val user = UserDomain(uid = "1", email = "test@test.com", name = "Test User")

    @Test
    fun `logIn should save user to local data source when successful`() = runTest {
        // Given
        val email = "test@test.com"
        val password = "password"
        coEvery { accountService.logIn(email, password) } returns user.right()

        // When
        val result = repository.logIn(email, password)

        // Then
        assertEquals(user.right(), result)
        coVerify { authLocalDataSource.newUser(user) }
    }

    @Test
    fun `logIn should not save user to local data source when fails`() = runTest {
        // Given
        val email = "test@test.com"
        val password = "password"
        val error = CustomError.Unknown("Login failed")
        coEvery { accountService.logIn(email, password) } returns error.left()

        // When
        val result = repository.logIn(email, password)

        // Then
        assertEquals(error.left(), result)
        coVerify(exactly = 0) { authLocalDataSource.newUser(any()) }
    }

    @Test
    fun `singUp should save user to local data source when successful`() = runTest {
        // Given
        val email = "test@test.com"
        val password = "password"
        coEvery { accountService.singUp(email, password) } returns user.right()

        // When
        val result = repository.singUp(email, password)

        // Then
        assertEquals(user.right(), result)
        coVerify { authLocalDataSource.newUser(user) }
    }

    @Test
    fun `singUp should not save user to local data source when fails`() = runTest {
        // Given
        val email = "test@test.com"
        val password = "password"
        val error = CustomError.Unknown("Signup failed")
        coEvery { accountService.singUp(email, password) } returns error.left()

        // When
        val result = repository.singUp(email, password)

        // Then
        assertEquals(error.left(), result)
        coVerify(exactly = 0) { authLocalDataSource.newUser(any()) }
    }
}
