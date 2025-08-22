package com.martorell.albert.meteomartocompose.data.auth.repositories.auth

import com.martorell.albert.meteomartocompose.data.ResultResponse
import com.martorell.albert.meteomartocompose.domain.auth.UserDomain
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    val userState: Flow<UserDomain>
    suspend fun singUp(
        email: String,
        password: String
    ): ResultResponse<UserDomain?>

    suspend fun logIn(
        email: String,
        password: String
    ): ResultResponse<UserDomain?>

    suspend fun userLogged(): Boolean

    fun logOut()

}