package com.martorell.albert.meteomartocompose.di.auth

import com.martorell.albert.meteomartocompose.data.auth.AccountServiceImpl
import com.martorell.albert.meteomartocompose.data.auth.repositories.auth.AuthRepository
import com.martorell.albert.meteomartocompose.data.auth.repositories.auth.AuthRepositoryImpl
import com.martorell.albert.meteomartocompose.data.auth.sources.auth.AccountService
import com.martorell.albert.meteomartocompose.data.auth.sources.auth.LocalDataSource
import com.martorell.albert.meteomartocompose.usecases.signup.SignUpInteractors
import com.martorell.albert.meteomartocompose.usecases.signup.SignUpUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class RegisterModule {

    @Provides
    fun signUpInteractorsProvider(
        signUpUseCase: SignUpUseCase
    ) = SignUpInteractors(
        signUpUseCase = signUpUseCase
    )

    @Provides
    fun providesAccountService(): AccountService {
        return AccountServiceImpl()
    }

    @Provides
    fun providesAuthRepository(
        accountService: AccountService,
        authLocalSource: LocalDataSource
    ): AuthRepository {
        return AuthRepositoryImpl(
            accountService = accountService,
            authLocalSource = authLocalSource
        )
    }

}