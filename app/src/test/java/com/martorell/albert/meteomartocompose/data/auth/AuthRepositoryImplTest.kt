package com.martorell.albert.meteomartocompose.data.auth

import arrow.core.left
import arrow.core.right
import com.martorell.albert.meteomartocompose.data.CustomError
import com.martorell.albert.meteomartocompose.domain.auth.UserDomain
import com.martorell.albert.meteomartocompose.testutil.fakes.FakeAccountService
import com.martorell.albert.meteomartocompose.testutil.fakes.FakeAuthLocalDataSource
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class AuthRepositoryImplTest {

    private val accountService = FakeAccountService()
    private val authLocalDataSource = FakeAuthLocalDataSource()
    private val repository = AuthRepositoryImpl(accountService, authLocalDataSource)

    private val user = UserDomain(uid = "1", email = "test@test.com", name = "Test User")

    @Test
    fun `logIn saves user locally on success and skips local save on failure`() = runTest {
        accountService.logInResult = user.right()
        val successResult = repository.logIn("test@test.com", "password")
        assertEquals(user.right(), successResult)
        assertEquals(listOf(user), authLocalDataSource.savedUsers)

        authLocalDataSource.savedUsers.clear()
        val error = CustomError.Unknown("Login failed")
        accountService.logInResult = error.left()
        val failureResult = repository.logIn("test@test.com", "wrong")
        assertEquals(error.left(), failureResult)
        assertEquals(emptyList<UserDomain>(), authLocalDataSource.savedUsers)
    }

    @Test
    fun `singUp saves user locally on success and skips local save on failure`() = runTest {
        accountService.signUpResult = user.right()
        val successResult = repository.singUp("test@test.com", "password")
        assertEquals(user.right(), successResult)
        assertEquals(listOf(user), authLocalDataSource.savedUsers)

        authLocalDataSource.savedUsers.clear()
        val error = CustomError.Unknown("Signup failed")
        accountService.signUpResult = error.left()
        val failureResult = repository.singUp("test@test.com", "password")
        assertEquals(error.left(), failureResult)
        assertEquals(emptyList<UserDomain>(), authLocalDataSource.savedUsers)
    }
}
