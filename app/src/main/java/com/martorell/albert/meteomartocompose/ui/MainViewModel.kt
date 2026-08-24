package com.martorell.albert.meteomartocompose.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martorell.albert.meteomartocompose.usecases.splash.SplashInteractors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val splashInteractors: SplashInteractors,
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()

    data class UiState(
        val isLoading: Boolean = true,
        val isLoggedIn: Boolean = false
    )

    fun checkAuthStatus(skipDelay: Boolean = false) {
        viewModelScope.launch {
            val loggedIn = splashInteractors.userLoggedUseCase()
            if (!skipDelay) {
                delay(timeMillis = 2000)
            }
            _state.update { it.copy(isLoading = false, isLoggedIn = loggedIn) }
        }
    }
}
