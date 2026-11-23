package com.martorell.albert.meteomartocompose.usecases.login

import com.martorell.albert.meteomartocompose.data.auth.repositories.AuthRepository
import com.martorell.albert.meteomartocompose.domain.auth.UserDomain
import com.martorell.albert.meteomartocompose.domain.model.DomainError
import com.martorell.albert.meteomartocompose.domain.model.Result
import javax.inject.Inject

class LogInUseCase @Inject constructor(private val authRepository: AuthRepository) {

    suspend operator fun invoke(
        email: String,
        password: String
    ): Result<UserDomain?, DomainError> =
        authRepository.logIn(
            email = email,
            password = password
        )

}