package com.martorell.albert.meteomartocompose.data

import arrow.core.Either
import arrow.core.right
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.martorell.albert.meteomartocompose.data.auth.AccountService
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountServiceImpl @Inject constructor() : AccountService {

    override suspend fun singUp(email: String, password: String):
            Either<CustomError, String?> =

        customTryCatch {
            val result = Firebase.auth.createUserWithEmailAndPassword(email, password).await()
            return result.user?.email.right()
        }

}