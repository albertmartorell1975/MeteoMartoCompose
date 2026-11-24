package com.martorell.albert.meteomartocompose.data.auth

import com.martorell.albert.meteomartocompose.data.auth.repositories.AuthRepository
import com.martorell.albert.meteomartocompose.data.auth.sources.AccountService
import com.martorell.albert.meteomartocompose.data.auth.sources.AuthLocalDataSource
import com.martorell.albert.meteomartocompose.domain.auth.UserDomain
import com.martorell.albert.meteomartocompose.domain.model.DomainError
import com.martorell.albert.meteomartocompose.domain.model.Result
import com.martorell.albert.meteomartocompose.domain.model.onSuccess
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val accountService: AccountService,
    private val authLocalDataSource: AuthLocalDataSource,
) : AuthRepository {

    override val isUserLoggedIn: Flow<Boolean>
        get() = accountService.authState

    override fun logOut() {
        accountService.signOut()
    }

    override suspend fun userLogged(): Boolean =
        accountService.hasUser()

    override suspend fun logIn(
        email: String,
        password: String
    ): Result<UserDomain?, DomainError> {
        val result = accountService.logIn(
            email = email,
            password = password
        )

        result.onSuccess { user ->
            user?.let { authLocalDataSource.newUser(it) }
        }

        return result
    }

    override suspend fun singUp(
        email: String,
        password: String
    ): Result<UserDomain?, DomainError> {
        val result = accountService.singUp(
            email = email,
            password = password
        )

        result.onSuccess { user ->
            user?.let { authLocalDataSource.newUser(it) }
        }

        return result
    }
}
