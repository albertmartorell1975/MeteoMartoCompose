package com.martorell.albert.meteomartocompose.data.auth

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.martorell.albert.meteomartocompose.data.auth.mappers.AuthErrorMapper
import com.martorell.albert.meteomartocompose.data.auth.sources.AccountService
import com.martorell.albert.meteomartocompose.data.util.safeCall
import com.martorell.albert.meteomartocompose.domain.auth.UserDomain
import com.martorell.albert.meteomartocompose.domain.model.DomainError
import com.martorell.albert.meteomartocompose.domain.model.Result
import com.martorell.albert.meteomartocompose.utils.toDomain
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountServiceImpl @Inject constructor(
    private val authErrorMapper: AuthErrorMapper,
) : AccountService {

    override val authState: Flow<Boolean> = callbackFlow {
        val auth = Firebase.auth
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser != null)
        }
        auth.addAuthStateListener(listener)
        awaitClose {
            auth.removeAuthStateListener(listener)
        }
    }

    override suspend fun hasUser(): Boolean =
        Firebase.auth.currentUser != null

    override suspend fun singUp(email: String, password: String): Result<UserDomain?, DomainError> =
        safeCall(mapper = authErrorMapper::map) {
            Firebase.auth.createUserWithEmailAndPassword(email, password).await().user?.toDomain()
        }

    override suspend fun logIn(email: String, password: String): Result<UserDomain?, DomainError> =
        safeCall(mapper = authErrorMapper::map) {
            Firebase.auth.signInWithEmailAndPassword(email, password).await().user?.toDomain()
        }

    override fun signOut() {
        Firebase.auth.signOut()
    }
}
