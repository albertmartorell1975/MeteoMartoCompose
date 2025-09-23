package com.martorell.albert.meteomartocompose.usecases.splash

import com.martorell.albert.meteomartocompose.data.auth.repositories.AuthRepository
import javax.inject.Inject

class UserLoggedUseCase @Inject constructor(private val authRepository: AuthRepository) {

    suspend fun invoke(): Boolean =
        authRepository.userLogged()

}