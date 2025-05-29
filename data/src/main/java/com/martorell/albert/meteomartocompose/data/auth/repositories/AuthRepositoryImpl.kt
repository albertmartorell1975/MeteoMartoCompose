package com.martorell.albert.meteomartocompose.data.auth.repositories

import arrow.core.Either
import com.martorell.albert.meteomartocompose.data.CustomError
import com.martorell.albert.meteomartocompose.data.auth.AccountService
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val accountService: AccountService) :
    AuthRepository {

    override suspend fun singUp(email: String, password: String): Either<CustomError, String?> =

        accountService.singUp(
            email = email,
            password = password
        )

}