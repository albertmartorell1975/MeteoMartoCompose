package com.martorell.albert.meteomartocompose.di.auth

import com.martorell.albert.meteomartocompose.data.auth.AccountServiceImpl
import com.martorell.albert.meteomartocompose.data.auth.AuthRoomDataSource
import com.martorell.albert.meteomartocompose.data.auth.repositories.auth.AuthRepository
import com.martorell.albert.meteomartocompose.data.auth.repositories.auth.AuthRepositoryImpl
import com.martorell.albert.meteomartocompose.data.auth.sources.auth.AccountService
import com.martorell.albert.meteomartocompose.data.auth.sources.auth.AuthLocalDataSource
import com.martorell.albert.meteomartocompose.framework.db.MeteoMartoDatabase
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
        authLocalDataSource: AuthLocalDataSource
    ): AuthRepository {
        return AuthRepositoryImpl(
            accountService = accountService,
            authLocalDataSource = authLocalDataSource
        )
    }

    @Provides
    fun providesAuthRoomDataSource(db: MeteoMartoDatabase): AuthLocalDataSource =
        AuthRoomDataSource(db)

}