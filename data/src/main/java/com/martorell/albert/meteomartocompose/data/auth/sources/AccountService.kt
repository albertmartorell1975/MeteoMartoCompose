package com.martorell.albert.meteomartocompose.data.auth.sources

import com.martorell.albert.meteomartocompose.domain.auth.UserDomain
import com.martorell.albert.meteomartocompose.domain.model.DomainError
import com.martorell.albert.meteomartocompose.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface AccountService {

    val authState: Flow<Boolean>

    suspend fun singUp(
        email: String,
        password: String
    ): Result<UserDomain?, DomainError>

    suspend fun logIn(
        email: String,
        password: String
    ): Result<UserDomain?, DomainError>

    suspend fun hasUser(): Boolean

    fun signOut()

}
