package com.martorell.albert.meteomartocompose.data.auth

import arrow.core.right
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.martorell.albert.meteomartocompose.data.ResultResponse
import com.martorell.albert.meteomartocompose.data.auth.sources.auth.AccountService
import com.martorell.albert.meteomartocompose.data.customTryCatch
import com.martorell.albert.meteomartocompose.domain.auth.UserDomain
import com.martorell.albert.meteomartocompose.utils.toDomain
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountServiceImpl @Inject constructor() : AccountService {

    override val currentUser: Flow<UserDomain>
        get() = callbackFlow {
            val listener = FirebaseAuth.AuthStateListener { auth ->
                auth.currentUser?.let {
                    UserDomain(
                        it.uid,
                        email = it.email,
                        name = it.displayName
                    )
                }?.let {
                    this.trySend(it)
                }
            }

            Firebase.auth.addAuthStateListener(listener)
            awaitClose { Firebase.auth.removeAuthStateListener { listener } }

        }

    override suspend fun hasUser(): Boolean =
        Firebase.auth.currentUser != null

    override suspend fun singUp(email: String, password: String):
            ResultResponse<UserDomain?> =

        customTryCatch {

            val result = Firebase.auth.createUserWithEmailAndPassword(
                email,
                password
            ).await()

            return result.user?.toDomain().right()

        }

    override suspend fun logIn(email: String, password: String): ResultResponse<UserDomain?> =

        customTryCatch {

            val result = Firebase.auth.signInWithEmailAndPassword(
                email,
                password
            ).await()

            return result.user?.toDomain().right()

        }

    override fun signOut() {

        Firebase.auth.signOut()

    }
}