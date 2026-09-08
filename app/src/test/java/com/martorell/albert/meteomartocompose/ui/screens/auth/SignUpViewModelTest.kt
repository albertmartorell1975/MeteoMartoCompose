package com.martorell.albert.meteomartocompose.ui.screens.auth

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import com.martorell.albert.meteomartocompose.data.CustomError
import com.martorell.albert.meteomartocompose.domain.auth.UserDomain
import com.martorell.albert.meteomartocompose.usecases.signup.SignUpInteractors
import com.martorell.albert.meteomartocompose.usecases.signup.SignUpUseCase
import com.martorell.albert.meteomartocompose.usecases.utils.ValidateEmailUseCase
import com.martorell.albert.meteomartocompose.usecases.utils.ValidatePasswordUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SignUpViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val validateEmailUseCase: ValidateEmailUseCase = mockk()
    private val validatePasswordUseCase: ValidatePasswordUseCase = mockk()
    private val signUpUseCase: SignUpUseCase = mockk()

    private val interactors = SignUpInteractors(
        validateEmailUseCase = validateEmailUseCase,
        validatePasswordUseCase = validatePasswordUseCase,
        signUpUseCase = signUpUseCase
    )

    private lateinit var viewModel: SignUpViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = SignUpViewModel(interactors)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() {
        val state = viewModel.state.value
        assertEquals("", state.email)
        assertEquals("", state.password)
        assertTrue(state.isEmailValid)
        assertTrue(state.isPasswordValid)
        assertFalse(state.loading)
        assertFalse(state.signUpChecked)
    }

    @Test
    fun `setEmail updates state and clears email error`() {
        val email = "test@example.com"
        viewModel.setEmail(email)
        assertEquals(email, viewModel.state.value.email)
        assertTrue(viewModel.state.value.isEmailValid)
    }

    @Test
    fun `validateEmail updates isEmailValid state when not empty`() {
        val email = "invalid"
        viewModel.setEmail(email)
        every { validateEmailUseCase(email) } returns false
        viewModel.validateEmail()
        assertFalse(viewModel.state.value.isEmailValid)
    }

    @Test
    fun `clearEmailError resets isEmailValid to true`() {
        val email = "some@email.com"
        viewModel.setEmail(email)
        every { validateEmailUseCase(email) } returns false
        viewModel.validateEmail()
        assertFalse(viewModel.state.value.isEmailValid)

        viewModel.clearEmailError()
        assertTrue(viewModel.state.value.isEmailValid)
    }

    @Test
    fun `performSignUp successful flow`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val user = UserDomain("1", email, "Name")

        viewModel.setEmail(email)
        viewModel.setPassword(password)

        every { validateEmailUseCase(email) } returns true
        every { validatePasswordUseCase(password) } returns true
        coEvery { signUpUseCase(email, password) } returns user.right()

        viewModel.performSignUp()
        testDispatcher.scheduler.advanceUntilIdle()

        val finalState = viewModel.state.value
        assertTrue(finalState.validUser)
        assertTrue(finalState.signUpChecked)
        assertFalse(finalState.loading)
    }

    @Test
    fun `performSignUp failure flow emits event`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val error = CustomError.Unknown("Error")

        viewModel.setEmail(email)
        viewModel.setPassword(password)

        every { validateEmailUseCase(email) } returns true
        every { validatePasswordUseCase(password) } returns true
        coEvery { signUpUseCase(email, password) } returns error.left()

        viewModel.events.test {
            viewModel.performSignUp()
            testDispatcher.scheduler.advanceUntilIdle()

            assertEquals(SignUpViewModel.SignUpEvent.SignUpError, awaitItem())
            val finalState = viewModel.state.value
            assertFalse(finalState.validUser)
            assertTrue(finalState.signUpChecked)
        }
    }

    @Test
    fun `performSignUp with local validation error does not call usecase`() = runTest {
        val email = "invalid"
        viewModel.setEmail(email)
        
        every { validateEmailUseCase(email) } returns false
        every { validatePasswordUseCase(any()) } returns true

        viewModel.performSignUp()
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 0) { signUpUseCase(any(), any()) }
        assertFalse(viewModel.state.value.isEmailValid)
        assertTrue(viewModel.state.value.showError)
    }
}
