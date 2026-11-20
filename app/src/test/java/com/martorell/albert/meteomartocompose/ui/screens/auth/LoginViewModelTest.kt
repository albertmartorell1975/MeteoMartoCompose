package com.martorell.albert.meteomartocompose.ui.screens.auth

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import com.martorell.albert.meteomartocompose.data.CustomError
import com.martorell.albert.meteomartocompose.domain.auth.UserDomain
import com.martorell.albert.meteomartocompose.testutil.MainDispatcherRule
import com.martorell.albert.meteomartocompose.testutil.fakes.FakeAuthRepository
import com.martorell.albert.meteomartocompose.usecases.login.LogInUseCase
import com.martorell.albert.meteomartocompose.usecases.login.LoginInteractors
import com.martorell.albert.meteomartocompose.usecases.login.ValidateLoginUseCase
import com.martorell.albert.meteomartocompose.usecases.utils.ValidateEmailUseCase
import com.martorell.albert.meteomartocompose.usecases.utils.ValidatePasswordUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val authRepository = FakeAuthRepository()
    private val validateEmailUseCase = ValidateEmailUseCase()
    private val validatePasswordUseCase = ValidatePasswordUseCase()
    private val viewModel = LoginViewModel(
        LoginInteractors(
            validateLoginUseCase = ValidateLoginUseCase(validateEmailUseCase, validatePasswordUseCase),
            validateEmailUseCase = validateEmailUseCase,
            validatePasswordUseCase = validatePasswordUseCase,
            logInUseCase = LogInUseCase(authRepository),
        )
    )

    @Test
    fun `initial state is passive and local validation failure skips repository call`() = runTest {
        val initial = viewModel.state.value
        assertEquals("", initial.email)
        assertEquals("", initial.password)
        assertTrue(initial.isEmailValid)
        assertTrue(initial.isPasswordValid)
        assertFalse(initial.loading)
        assertFalse(initial.loginChecked)

        viewModel.setEmail("invalid")
        viewModel.validateEmail()
        assertFalse(viewModel.state.value.isEmailValid)
        viewModel.clearEmailError()
        assertTrue(viewModel.state.value.isEmailValid)

        viewModel.setPassword("password123")
        viewModel.performLogin()
        advanceUntilIdle()

        assertTrue(authRepository.logInCalls.isEmpty())
        assertFalse(viewModel.state.value.isEmailValid)
        assertTrue(viewModel.state.value.showError)
        assertFalse(viewModel.state.value.loginChecked)
    }

    @Test
    fun `performLogin transitions to authenticated user on success`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        authRepository.logInResult = UserDomain("1", email, "Name").right()

        viewModel.setEmail(email)
        viewModel.setPassword(password)
        viewModel.performLogin()
        advanceUntilIdle()

        val finalState = viewModel.state.value
        assertTrue(finalState.validUser)
        assertTrue(finalState.loginChecked)
        assertTrue(finalState.loginStatus)
        assertFalse(finalState.loading)
        assertEquals(listOf(email to password), authRepository.logInCalls)
    }

    @Test
    fun `performLogin emits LoginError event and updates error state on failure`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val error = CustomError.Unknown("Error")
        authRepository.logInResult = error.left()

        viewModel.setEmail(email)
        viewModel.setPassword(password)

        viewModel.events.test {
            viewModel.performLogin()
            advanceUntilIdle()

            assertEquals(LoginViewModel.LoginEvent.LoginError(error), awaitItem())
            val finalState = viewModel.state.value
            assertFalse(finalState.validUser)
            assertTrue(finalState.loginChecked)
            assertTrue(finalState.showError)
            assertEquals(error, finalState.error)
        }
    }
}
