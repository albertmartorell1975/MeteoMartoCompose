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
        val loginStatus: Boolean = false
    )

    fun checkLogin(user: String, password: String) {

        viewModelScope.launch {
            _state.value = UiState(
                loading = true
            )
            delay(2000)
            _state.value = UiState(
                loginChecked = true,
                loginStatus = loginInteractors.validateLoginUseCase.invoke(
                    user = user,
                    password = password
                )
            )
        }

    }

    fun loginUnchecked() {

        _state.value = UiState(loginChecked = false)

    }

}