package com.martorell.albert.meteomartocompose.data.auth

import com.martorell.albert.meteomartocompose.data.auth.sources.AuthLocalDataSource
import com.martorell.albert.meteomartocompose.domain.auth.UserDomain
import com.martorell.albert.meteomartocompose.framework.db.MeteoMartoDatabase
import com.martorell.albert.meteomartocompose.utils.toRoom
import javax.inject.Inject

class AuthRoomDataSource @Inject constructor(db: MeteoMartoDatabase) : AuthLocalDataSource {

    private val userDao = db.userDao()

    override suspend fun newUser(user: UserDomain) {
        userDao.insertUser(user.toRoom())
    }
}
