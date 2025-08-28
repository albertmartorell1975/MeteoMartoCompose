package com.martorell.albert.meteomartocompose.usecases.signup

import com.martorell.albert.meteomartocompose.data.ResultResponse
import com.martorell.albert.meteomartocompose.data.auth.repositories.auth.AuthRepository
import com.martorell.albert.meteomartocompose.domain.auth.UserDomain
import javax.inject.Inject

class SignUpUseCase @Inject constructor(private val authRepository: AuthRepository) {

    suspend fun invoke(
        email: String,
        password: String
    ): ResultResponse<UserDomain?> =

        authRepository.singUp(
            email = email,
            password = password
        )

}