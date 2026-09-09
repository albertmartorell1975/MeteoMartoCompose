package com.martorell.albert.meteomartocompose.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martorell.albert.meteomartocompose.data.CustomError
import com.martorell.albert.meteomartocompose.usecases.signup.SignUpInteractors
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
class SignUpViewModel @Inject constructor(
    private val signUpInteractors: SignUpInteractors
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()

    private val _events = Channel<SignUpEvent>(Channel.BUFFERED)
    val events: Flow<SignUpEvent> = _events.receiveAsFlow()

    sealed interface SignUpEvent {
        data class SignUpError(val error: CustomError) : SignUpEvent
    }

    data class UiState(
        val loading: Boolean = false,
        val signUpChecked: Boolean = false,
        val signUpStatus: Boolean = false,
        val email: String = "",
        val isEmailValid: Boolean = true,
        val password: String = "",
        val isPasswordValid: Boolean = true,
        val passwordVisible: Boolean = false,
        val showError: Boolean = false,
        val validUser: Boolean = false,
        val error: CustomError? = null,
    )

    fun performSignUp() {
        viewModelScope.launch {
            val currentEmail = _state.value.email
            val currentPassword = _state.value.password

            val isEmailValid = signUpInteractors.validateEmailUseCase(currentEmail)
            val isPasswordValid = signUpInteractors.validatePasswordUseCase(currentPassword)

            if (!isEmailValid || !isPasswordValid) {
                _state.update {
                    it.copy(
                        isEmailValid = isEmailValid,
                        isPasswordValid = isPasswordValid,
                        showError = true,
                        signUpChecked = false
                    )
                }
                return@launch
            }

            _state.update { it.copy(loading = true, signUpChecked = false, showError = false) }

            // Simulate network delay
            delay(2000.milliseconds)

            val result = signUpInteractors.signUpUseCase.invoke(
                email = currentEmail,
                password = currentPassword
            )

            result.fold({ customError ->
                _state.update {
                    it.copy(
                        validUser = false,
                        loading = false,
                        signUpChecked = true,
                        showError = true,
                        signUpStatus = false,
                        error = customError
                    )
                }
                viewModelScope.launch { _events.send(SignUpEvent.SignUpError(customError)) }
            }) {
                _state.update {
                    it.copy(
                        validUser = true,
                        signUpChecked = true,
                        loading = false,
                        signUpStatus = true
                    )
                }
            }
        }
    }

    fun signUpUnchecked() {
        _state.update { it.copy(signUpChecked = false) }
    }

    fun validateEmail() {
        if (_state.value.email.isNotEmpty()) {
            _state.update {
                it.copy(isEmailValid = signUpInteractors.validateEmailUseCase(_state.value.email))
            }
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
                it.copy(isPasswordValid = signUpInteractors.validatePasswordUseCase(_state.value.password))
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
        return signUpInteractors.validateEmailUseCase(state.email) &&
                signUpInteractors.validatePasswordUseCase(state.password)
    }
}
