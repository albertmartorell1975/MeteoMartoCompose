package com.martorell.albert.meteomartocompose.data.auth

import com.martorell.albert.meteomartocompose.domain.auth.UserDomain
import com.martorell.albert.meteomartocompose.domain.model.DomainError
import com.martorell.albert.meteomartocompose.domain.model.Result
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
        accountService.logInResult = Result.Success(user)
        val successResult = repository.logIn("test@test.com", "password")
        assertEquals(Result.Success(user), successResult)
        assertEquals(listOf(user), authLocalDataSource.savedUsers)

        authLocalDataSource.savedUsers.clear()
        val error = DomainError.Auth.InvalidCredentials
        accountService.logInResult = Result.Error(error)
        val failureResult = repository.logIn("test@test.com", "wrong")
        assertEquals(Result.Error(error), failureResult)
        assertEquals(emptyList<UserDomain>(), authLocalDataSource.savedUsers)
    }

    @Test
    fun `singUp saves user locally on success and skips local save on failure`() = runTest {
        accountService.signUpResult = Result.Success(user)
        val successResult = repository.singUp("test@test.com", "password")
        assertEquals(Result.Success(user), successResult)
        assertEquals(listOf(user), authLocalDataSource.savedUsers)

        authLocalDataSource.savedUsers.clear()
        val error = DomainError.Auth.EmailAlreadyInUse
        accountService.signUpResult = Result.Error(error)
        val failureResult = repository.singUp("test@test.com", "password")
        assertEquals(Result.Error(error), failureResult)
        assertEquals(emptyList<UserDomain>(), authLocalDataSource.savedUsers)
    }
}
