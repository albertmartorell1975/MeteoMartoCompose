package com.martorell.albert.meteomartocompose.usecases.signup

import arrow.core.Either
import com.martorell.albert.meteomartocompose.data.CustomError
import com.martorell.albert.meteomartocompose.data.auth.repositories.AuthRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(private val authRepository: AuthRepository) {

    suspend fun invoke(email: String, password: String): Either<CustomError, String?> =

        authRepository.singUp(
            email = email,
            password = password
        )

}