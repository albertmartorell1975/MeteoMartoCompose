package com.martorell.albert.meteomartocompose.data.auth.repositories.auth

import com.martorell.albert.meteomartocompose.data.ResultResponse
import com.martorell.albert.meteomartocompose.data.auth.sources.auth.AccountService
import com.martorell.albert.meteomartocompose.data.auth.sources.auth.AuthLocalDataSource
import com.martorell.albert.meteomartocompose.domain.auth.UserDomain
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val accountService: AccountService,
    private val authLocalDataSource: AuthLocalDataSource
) :
    AuthRepository {

    override fun logOut() {
        accountService.signOut()
    }

    override suspend fun userLogged(): Boolean =
        accountService.hasUser()

    override suspend fun logIn(
        email: String,
        password: String
    ): ResultResponse<UserDomain?> {

        val result = accountService.logIn(
            email = email,
            password = password
        )

        // Only if the result is correct, will be saved on the local database
        result.fold(
            { },
            {
                if (it != null) {
                    authLocalDataSource.newUser(it)
                }
            }
        )

        return result

    }

    override suspend fun singUp(
        email: String,
        password: String
    ): ResultResponse<UserDomain?> {

        val result = accountService.singUp(
            email = email,
            password = password
        )

        // Only if the result is correct, will be saved on the local database
        result.fold(
            {},
            {
                if (it != null) {
                    authLocalDataSource.newUser(it)
                }
            }
        )

        return result

    }

}