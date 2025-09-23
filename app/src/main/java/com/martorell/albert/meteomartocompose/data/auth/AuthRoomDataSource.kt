package com.martorell.albert.meteomartocompose.data.auth

import com.martorell.albert.meteomartocompose.data.auth.sources.AuthLocalDataSource
import com.martorell.albert.meteomartocompose.domain.auth.UserDomain
import com.martorell.albert.meteomartocompose.framework.db.MeteoMartoDatabase
import com.martorell.albert.meteomartocompose.utils.toRoom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRoomDataSource(db: MeteoMartoDatabase) : AuthLocalDataSource {

    private val userDao = db.userDao()

    override suspend fun newUser(user: UserDomain) {

        withContext(Dispatchers.IO) {
            userDao.insertUser(user.toRoom())
        }

    }

}