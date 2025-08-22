package com.martorell.albert.meteomartocompose.usecases.cityweather

import com.martorell.albert.meteomartocompose.data.auth.repositories.auth.AuthRepository
import com.martorell.albert.meteomartocompose.domain.auth.UserDomain
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserFirebaseUseCase @Inject constructor(private val authRepository: AuthRepository) {

    fun invoke(): Flow<UserDomain> =
        authRepository.userState

}