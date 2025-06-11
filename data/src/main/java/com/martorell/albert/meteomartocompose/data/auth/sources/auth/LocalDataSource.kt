package com.martorell.albert.meteomartocompose.data.auth.sources.auth

import com.martorell.albert.meteomartocompose.domain.auth.UserDomain

interface LocalDataSource {

    suspend fun newUser(user: UserDomain)

}