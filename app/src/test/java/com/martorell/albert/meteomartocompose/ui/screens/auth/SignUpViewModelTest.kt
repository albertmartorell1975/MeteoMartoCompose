package com.martorell.albert.meteomartocompose.ui.screens.auth

import app.cash.turbine.test
import com.martorell.albert.meteomartocompose.domain.auth.UserDomain
import com.martorell.albert.meteomartocompose.domain.model.DomainError
import com.martorell.albert.meteomartocompose.domain.model.Result
import com.martorell.albert.meteomartocompose.testutil.MainDispatcherRule
import com.martorell.albert.meteomartocompose.testutil.fakes.FakeAuthRepository
import com.martorell.albert.meteomartocompose.usecases.signup.SignUpInteractors
import com.martorell.albert.meteomartocompose.usecases.signup.SignUpUseCase
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
class SignUpViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val authRepository = FakeAuthRepository()
    private val viewModel = SignUpViewModel(
        SignUpInteractors(
            validateEmailUseCase = ValidateEmailUseCase(),
            validatePasswordUseCase = ValidatePasswordUseCase(),
            signUpUseCase = SignUpUseCase(authRepository),
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
        assertFalse(initial.signUpChecked)

        viewModel.setEmail("invalid")
        viewModel.validateEmail()
        assertFalse(viewModel.state.value.isEmailValid)
        viewModel.clearEmailError()
        assertTrue(viewModel.state.value.isEmailValid)

        viewModel.setPassword("password123")
        viewModel.performSignUp()
        advanceUntilIdle()

        assertTrue(authRepository.signUpCalls.isEmpty())
        assertFalse(viewModel.state.value.isEmailValid)
        assertTrue(viewModel.state.value.showError)
        assertFalse(viewModel.state.value.signUpChecked)
    }

    @Test
    fun `performSignUp transitions to valid user on success`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        authRepository.signUpResult = Result.Success(UserDomain("1", email, "Name"))

        viewModel.setEmail(email)
        viewModel.setPassword(password)
        viewModel.performSignUp()
        advanceUntilIdle()

        val finalState = viewModel.state.value
        assertTrue(finalState.validUser)
        assertTrue(finalState.signUpChecked)
        assertTrue(finalState.signUpStatus)
        assertFalse(finalState.loading)
        assertEquals(listOf(email to password), authRepository.signUpCalls)
    }

    @Test
    fun `performSignUp emits SignUpError event and updates error state on failure`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val error = DomainError.Auth.EmailAlreadyInUse
        authRepository.signUpResult = Result.Error(error)

        viewModel.setEmail(email)
        viewModel.setPassword(password)

        viewModel.events.test {
            viewModel.performSignUp()
            advanceUntilIdle()

            assertEquals(SignUpViewModel.SignUpEvent.SignUpError(error), awaitItem())
            val finalState = viewModel.state.value
            assertFalse(finalState.validUser)
            assertTrue(finalState.signUpChecked)
            assertTrue(finalState.showError)
            assertEquals(error, finalState.error)
        }
    }
}
