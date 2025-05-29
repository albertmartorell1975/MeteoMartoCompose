package com.martorell.albert.meteomartocompose.data.auth

import com.martorell.albert.meteomartocompose.data.ResultResponse

interface AccountService {

    suspend fun singUp(
        email: String,
        password: String
    ): ResultResponse<String?>
}