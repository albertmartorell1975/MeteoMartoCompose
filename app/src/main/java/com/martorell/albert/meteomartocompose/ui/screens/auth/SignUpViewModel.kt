package com.martorell.albert.meteomartocompose.ui.screens.auth

import androidx.lifecycle.ViewModel
import com.martorell.albert.meteomartocompose.data.CustomError
import com.martorell.albert.meteomartocompose.usecases.signup.SignUpInteractors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val signUpInteractors: SignUpInteractors) :
    ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()

    data class UiState(
        val loading: Boolean = false,
        val signUpChecked: Boolean = false,
        val signUpStatus: Boolean = false,
        val email: String = "",
        val password: String = "",
        val passwordVisible: Boolean = false,
        val showError: Boolean = false,
        val validUser: Boolean = false,
        val error: CustomError? = null,
        //val newUser: ResultResponse<String> = Either.Right("")
        //val newUser: ResultResponse<List<String>> = Either.Right(emptyList())
    )

    fun buttonEnabled(): Boolean =
        _state.value.email.isNotEmpty() && _state.value.password.isNotEmpty()

    fun signUpUnchecked() {

        val tmpState = _state.value
        val updatedState = tmpState.copy(
            signUpChecked = false
        )
        _state.value = updatedState

    }

    fun setUser(user: String) {

        val tmpState = _state.value
        val updatedState = tmpState.copy(email = user)
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

    suspend fun signUpClicked(email: String, password: String) {

        var tmpState = _state.value
        var updatedState = tmpState.copy(
            loading = true
        )
        _state.value = updatedState
        delay(2000)

        val result = signUpInteractors.signUpUseCase.invoke(
            email = email,
            password = password
        )

        result.fold({
            tmpState = _state.value
            updatedState = tmpState.copy(
                validUser = false,
                loading = false,
                signUpChecked = true,
                error = it
            )
            _state.value = updatedState

        }) {
            tmpState = _state.value
            updatedState = tmpState.copy(
                validUser = true,
                signUpChecked = true
            )
            _state.value = updatedState
        }

    }

}