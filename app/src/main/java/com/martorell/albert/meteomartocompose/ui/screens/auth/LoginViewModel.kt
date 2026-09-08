package com.martorell.albert.meteomartocompose.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martorell.albert.meteomartocompose.usecases.login.LoginInteractors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginInteractors: LoginInteractors
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()

    private val _events = Channel<LoginEvent>(Channel.BUFFERED)
    val events: Flow<LoginEvent> = _events.receiveAsFlow()

    sealed interface LoginEvent {
        object LoginError : LoginEvent
    }

    data class UiState(
        val loading: Boolean = false,
        val loginChecked: Boolean = false,
        val loginStatus: Boolean = false,
        val email: String = "",
        val isEmailValid: Boolean = true,
        val password: String = "",
        val isPasswordValid: Boolean = true,
        val passwordVisible: Boolean = false,
        val showError: Boolean = false,
        val validUser: Boolean = false
    )

    fun performLogin() {
        viewModelScope.launch {
            val currentEmail = _state.value.email
            val currentPassword = _state.value.password

            val isEmailValid = loginInteractors.validateEmailUseCase(currentEmail)
            val isPasswordValid = loginInteractors.validatePasswordUseCase(currentPassword)

            if (!isEmailValid || !isPasswordValid) {
                _state.update {
                    it.copy(
                        isEmailValid = isEmailValid,
                        isPasswordValid = isPasswordValid,
                        showError = true,
                        loginChecked = false // Do not show snackbar for local validation errors
                    )
                }
                return@launch
            }

            _state.update { it.copy(loading = true, loginChecked = false, showError = false) }

            // Simulate network delay
            delay(2000.milliseconds)

            val result = loginInteractors.logInUseCase.invoke(
                email = currentEmail,
                password = currentPassword
            )

            result.fold({
                _state.update {
                    it.copy(
                        validUser = false,
                        loading = false,
                        loginChecked = true,
                        showError = true,
                        loginStatus = false
                    )
                }
                viewModelScope.launch { _events.send(LoginEvent.LoginError) }
            }) {
                _state.update {
                    it.copy(
                        validUser = true,
                        loginChecked = true,
                        loading = false,
                        loginStatus = true
                    )
                }
            }
        }
    }

    fun loginUnchecked() {
        _state.update { it.copy(loginChecked = false) }
    }

    fun validateEmail() {
        if (_state.value.email.isNotEmpty()) {
            _state.update { it.copy(isEmailValid = loginInteractors.validateEmailUseCase(_state.value.email)) }
        }
    }

    fun clearEmailError() {
        _state.update { it.copy(isEmailValid = true) }
    }

    fun setEmail(email: String) {
        _state.update { it.copy(email = email, isEmailValid = true) }
    }

    fun validatePassword() {
        if (_state.value.password.isNotEmpty()) {
            _state.update {
                it.copy(isPasswordValid = loginInteractors.validatePasswordUseCase(_state.value.password))
            }
        }
    }

    fun clearPasswordError() {
        _state.update { it.copy(isPasswordValid = true) }
    }

    fun setPassword(password: String) {
        _state.update { it.copy(password = password, isPasswordValid = true) }
    }

    fun setPasswordVisible(visibility: Boolean) {
        _state.update { it.copy(passwordVisible = visibility) }
    }

    fun buttonEnabled(): Boolean {
        val state = _state.value
        return loginInteractors.validateEmailUseCase(state.email) &&
                loginInteractors.validatePasswordUseCase(state.password)
    }
}
