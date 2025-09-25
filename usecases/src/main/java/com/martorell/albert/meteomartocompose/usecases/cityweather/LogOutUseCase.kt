package com.martorell.albert.meteomartocompose.usecases.cityweather

import com.martorell.albert.meteomartocompose.data.auth.repositories.AuthRepository
import javax.inject.Inject

class LogOutUseCase @Inject constructor(private val authRepository: AuthRepository) {

     fun invoke() {

        authRepository.logOut()

    }

}