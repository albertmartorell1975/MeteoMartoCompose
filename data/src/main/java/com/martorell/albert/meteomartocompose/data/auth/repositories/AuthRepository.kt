package com.martorell.albert.meteomartocompose.data.auth.repositories

import com.martorell.albert.meteomartocompose.domain.auth.UserDomain
import com.martorell.albert.meteomartocompose.domain.model.DomainError
import com.martorell.albert.meteomartocompose.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    val isUserLoggedIn: Flow<Boolean>

    suspend fun singUp(
        email: String,
        password: String
    ): Result<UserDomain?, DomainError>

    suspend fun logIn(
        email: String,
        password: String
    ): Result<UserDomain?, DomainError>

    suspend fun userLogged(): Boolean

    fun logOut()

}
