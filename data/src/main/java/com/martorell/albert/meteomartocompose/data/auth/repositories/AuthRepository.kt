package com.martorell.albert.meteomartocompose.data.auth.repositories

import com.martorell.albert.meteomartocompose.data.ResultResponse
import com.martorell.albert.meteomartocompose.domain.auth.UserDomain

interface AuthRepository {

    suspend fun singUp(
        email: String,
        password: String
    ): ResultResponse<UserDomain?>

}