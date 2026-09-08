package com.martorell.albert.meteomartocompose.ui.screens.auth

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import com.martorell.albert.meteomartocompose.data.CustomError
import com.martorell.albert.meteomartocompose.domain.auth.UserDomain
import com.martorell.albert.meteomartocompose.usecases.login.LoginInteractors
import com.martorell.albert.meteomartocompose.usecases.login.LogInUseCase
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
class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    
    private val validateEmailUseCase: ValidateEmailUseCase = mockk()
    private val validatePasswordUseCase: ValidatePasswordUseCase = mockk()
    private val loginUseCase: LogInUseCase = mockk()
    
    private val interactors = LoginInteractors(
        validateLoginUseCase = mockk(),
        validateEmailUseCase = validateEmailUseCase,
        validatePasswordUseCase = validatePasswordUseCase,
        logInUseCase = loginUseCase
    )
    
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(interactors)
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
        assertFalse(state.loginChecked)
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
    fun `performLogin success flow`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val user = UserDomain("1", email, "Name")

        viewModel.setEmail(email)
        viewModel.setPassword(password)

        every { validateEmailUseCase(email) } returns true
        every { validatePasswordUseCase(password) } returns true
        coEvery { loginUseCase(email, password) } returns user.right()

        viewModel.performLogin()
        testDispatcher.scheduler.advanceUntilIdle()

        val finalState = viewModel.state.value
        assertTrue(finalState.validUser)
        assertTrue(finalState.loginChecked)
        assertFalse(finalState.loading)
    }

    @Test
    fun `performLogin failure flow emits event`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val error = CustomError.Unknown("Error")

        viewModel.setEmail(email)
        viewModel.setPassword(password)

        every { validateEmailUseCase(email) } returns true
        every { validatePasswordUseCase(password) } returns true
        coEvery { loginUseCase(email, password) } returns error.left()

        viewModel.events.test {
            viewModel.performLogin()
            testDispatcher.scheduler.advanceUntilIdle()

            assertEquals(LoginViewModel.LoginEvent.LoginError, awaitItem())
            val finalState = viewModel.state.value
            assertFalse(finalState.validUser)
            assertTrue(finalState.loginChecked)
        }
    }

    @Test
    fun `performLogin with local validation error does not call usecase`() = runTest {
        val email = "invalid"
        viewModel.setEmail(email)
        
        every { validateEmailUseCase(email) } returns false
        every { validatePasswordUseCase(any()) } returns true

        viewModel.performLogin()
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 0) { loginUseCase(any(), any()) }
        assertFalse(viewModel.state.value.isEmailValid)
        assertTrue(viewModel.state.value.showError)
    }
}
