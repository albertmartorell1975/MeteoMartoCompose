package com.martorell.albert.meteomartocompose.data.auth

import arrow.core.Either
import com.martorell.albert.meteomartocompose.data.CustomError

interface AccountService {

    suspend fun singUp(
        email: String,
        password: String
    ): Either<CustomError, String?>
}