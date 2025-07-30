package com.martorell.albert.meteomartocompose.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martorell.albert.meteomartocompose.usecases.splash.SplashInteractors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(val splashInteractors: SplashInteractors) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()

    data class UiState(
        val userLogged: Boolean = false
    )

    init {

        viewModelScope.launch {
            isUserLogged()
        }

    }

    private suspend fun isUserLogged() {

        val result = splashInteractors.userLoggedUseCase.invoke()

        _state.update {
            it.copy(
                userLogged = result,
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

}