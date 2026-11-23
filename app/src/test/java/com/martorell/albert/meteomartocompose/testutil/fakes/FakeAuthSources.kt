package com.martorell.albert.meteomartocompose.testutil.fakes

import com.martorell.albert.meteomartocompose.data.auth.repositories.AuthRepository
import com.martorell.albert.meteomartocompose.data.auth.sources.AccountService
import com.martorell.albert.meteomartocompose.data.auth.sources.AuthLocalDataSource
import com.martorell.albert.meteomartocompose.domain.auth.UserDomain
import com.martorell.albert.meteomartocompose.domain.model.DomainError
import com.martorell.albert.meteomartocompose.domain.model.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeAccountService : AccountService {

    val authStateFlow = MutableStateFlow(false)
    var logInResult: Result<UserDomain?, DomainError> =
        Result.Success(UserDomain(uid = "1", email = "test@test.com", name = "Test User"))
    var signUpResult: Result<UserDomain?, DomainError> =
        Result.Success(UserDomain(uid = "1", email = "test@test.com", name = "Test User"))
    var hasUserResult: Boolean = false
    var signOutCallCount: Int = 0
    val logInCalls = mutableListOf<Pair<String, String>>()
    val signUpCalls = mutableListOf<Pair<String, String>>()

    override val authState: Flow<Boolean>
        get() = authStateFlow

    override suspend fun singUp(email: String, password: String): Result<UserDomain?, DomainError> {
        signUpCalls += email to password
        return signUpResult
    }

    override suspend fun logIn(email: String, password: String): Result<UserDomain?, DomainError> {
        logInCalls += email to password
        return logInResult
    }

    override suspend fun hasUser(): Boolean = hasUserResult

    override fun signOut() {
        signOutCallCount++
    }
}

class FakeAuthLocalDataSource : AuthLocalDataSource {

    val savedUsers = mutableListOf<UserDomain>()

    override suspend fun newUser(user: UserDomain) {
        savedUsers += user
    }
}

class FakeAuthRepository : AuthRepository {

    val isUserLoggedInFlow = MutableStateFlow(false)
    var logInResult: Result<UserDomain?, DomainError> =
        Result.Success(UserDomain(uid = "1", email = "test@test.com", name = "Test User"))
    var signUpResult: Result<UserDomain?, DomainError> =
        Result.Success(UserDomain(uid = "1", email = "test@test.com", name = "Test User"))
    var isLoggedIn: Boolean = false
    var logOutCallCount: Int = 0
    val logInCalls = mutableListOf<Pair<String, String>>()
    val signUpCalls = mutableListOf<Pair<String, String>>()

    override val isUserLoggedIn: Flow<Boolean>
        get() = isUserLoggedInFlow

    override suspend fun singUp(email: String, password: String): Result<UserDomain?, DomainError> {
        signUpCalls += email to password
        return signUpResult
    }

    override suspend fun logIn(email: String, password: String): Result<UserDomain?, DomainError> {
        logInCalls += email to password
        return logInResult
    }

    override suspend fun userLogged(): Boolean = isLoggedIn

    override fun logOut() {
        logOutCallCount++
    }
}
