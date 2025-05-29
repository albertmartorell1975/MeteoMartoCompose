package com.martorell.albert.meteomartocompose.data.auth.repositories

import arrow.core.Either
import com.martorell.albert.meteomartocompose.data.CustomError

interface AuthRepository {

    suspend fun singUp(
        email: String,
        password: String
    ): Either<CustomError, String?>

}