package com.martorell.albert.meteomartocompose.data.auth.sources

import com.martorell.albert.meteomartocompose.data.ResultResponse
import com.martorell.albert.meteomartocompose.domain.auth.UserDomain

interface AccountService {

    suspend fun singUp(
        email: String,
        password: String
    ): ResultResponse<UserDomain?>

    suspend fun logIn(
        email: String,
        password: String
    ): ResultResponse<UserDomain?>

}