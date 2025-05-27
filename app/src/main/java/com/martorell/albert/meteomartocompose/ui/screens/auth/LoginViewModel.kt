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
        val user: String = "",
        val password: String = "",
        val passwordVisible: Boolean = false,
        val showError: Boolean = false
    )

    fun checkLogin() {

        viewModelScope.launch {

            val tmpState = _state.value
            var updatedState = tmpState.copy(
                loading = true,
            )

            _state.value = updatedState
            delay(2000)

            updatedState = tmpState.copy(
                loginChecked = true,
                loginStatus = loginInteractors.validateLoginUseCase.invoke(
                    user = _state.value.user,
                    password = _state.value.password,
                ),
                showError = !state.value.loginStatus && state.value.loginChecked
            )

            _state.value = updatedState
        }

    }

    fun buttonEnabled(): Boolean =
        _state.value.user.isNotEmpty() && _state.value.password.isNotEmpty()

    fun loginUnchecked() {

        val tmpState = _state.value
        val updatedState = tmpState.copy(
            loginChecked = false
        )
        _state.value = updatedState

    }

    fun setUser(user: String) {

        val tmpState = _state.value
        val updatedState = tmpState.copy(user = user)
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

}