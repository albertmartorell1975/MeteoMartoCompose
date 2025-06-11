package com.martorell.albert.meteomartocompose.usecases.login

import com.martorell.albert.meteomartocompose.data.ResultResponse
import com.martorell.albert.meteomartocompose.data.auth.repositories.auth.AuthRepository
import com.martorell.albert.meteomartocompose.domain.auth.UserDomain
import javax.inject.Inject

class LogInUseCase @Inject constructor(private val authRepository: AuthRepository) {

    suspend fun invoke(
        email: String,
        password: String
    ): ResultResponse<UserDomain?> =

        authRepository.logIn(
            email = email,
            password = password
        )

}