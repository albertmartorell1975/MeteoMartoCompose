package com.martorell.albert.meteomartocompose.data.auth.repositories

import com.martorell.albert.meteomartocompose.data.ResultResponse
import com.martorell.albert.meteomartocompose.data.auth.sources.AccountService
import com.martorell.albert.meteomartocompose.data.auth.sources.LocalDataSource
import com.martorell.albert.meteomartocompose.domain.auth.UserDomain
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val accountService: AccountService,
    private val authLocalSource: LocalDataSource
) :
    AuthRepository {

    override suspend fun singUp(
        email: String,
        password: String
    ): ResultResponse<UserDomain?> {

        val result = accountService.singUp(
            email = email,
            password = password
        )

        // Only if the result is correct, will be saved on the local database
        if (result.isRight())
            authLocalSource.newUser(result.getOrNull())

        return result

        /*result.fold(
            {},
            {
                authLocalSource.newUser(it)
            }
        )

         */

    }

}