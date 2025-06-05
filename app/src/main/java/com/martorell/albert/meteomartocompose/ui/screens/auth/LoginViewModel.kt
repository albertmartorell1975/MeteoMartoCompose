package com.martorell.albert.meteomartocompose.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martorell.albert.meteomartocompose.usecases.login.LoginInteractors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val loginInteractors: LoginInteractors) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()

    data class UiState(
        val loading: Boolean = false,
        val loginChecked: Boolean = false,
        val loginStatus: Boolean = false,
        val email: String = "",
        val password: String = "",
        val passwordVisible: Boolean = false,
        val showError: Boolean = false,
        val validUser: Boolean = false
    )

    fun checkLogin() {

        viewModelScope.launch {

            val tmpState = _state.value
            var updatedState = tmpState.copy(
                loading = true,
            )

            _state.value = updatedState

            updatedState = tmpState.copy(
                loginChecked = true,
                loginStatus = loginInteractors.validateLoginUseCase.invoke(
                    email = _state.value.email,
                    password = _state.value.password,
                ),
                showError = !state.value.loginStatus && state.value.loginChecked
            )

            _state.value = updatedState
        }

    }

    fun buttonEnabled(): Boolean =
        _state.value.email.isNotEmpty() && _state.value.password.isNotEmpty()

    fun loginUnchecked() {

        val tmpState = _state.value
        val updatedState = tmpState.copy(
            loginChecked = false
        )
        _state.value = updatedState

    }

    fun setEmail(email: String) {

        val tmpState = _state.value
        val updatedState = tmpState.copy(email = email)
        _state.value = updatedState

    }

    fun setPassword(password: String) {

        val tmpState = _state.value
        val updatedState = tmpState.copy(password = password)
        _state.value = updatedState

    }

    fun setPasswordVisible(visibility: Boolean) {

        val tmpState = _state.value
        val updatedState = tmpState.copy(
            passwordVisible = visibility
        )
        _state.value = updatedState

    }

    suspend fun logInClicked(
        email: String,
        password: String
    ) {

        var tmpState = _state.value
        var updatedState = tmpState.copy(
            loading = true
        )
        _state.value = updatedState
        delay(2000)

        val result = loginInteractors.logInUseCase.invoke(
            email = email,
            password = password
        )

        result.fold({
            tmpState = _state.value
            updatedState = tmpState.copy(
                validUser = false,
                loading = false,
                loginChecked = true,
                showError = false,
                loginStatus = false
            )
            _state.value = updatedState

        }) {

            tmpState = _state.value
            updatedState = tmpState.copy(
                validUser = true,
                loginChecked = true,
                loading = false
            )
            _state.value = updatedState
        }

    }

}