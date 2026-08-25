package com.martorell.albert.meteomartocompose.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.martorell.albert.meteomartocompose.BuildConfig
import com.martorell.albert.meteomartocompose.data.city.CityWeatherServerDataSource
import com.martorell.albert.meteomartocompose.data.city.repositories.PermissionChecker
import com.martorell.albert.meteomartocompose.data.city.repositories.PermissionRepository
import com.martorell.albert.meteomartocompose.data.cityweather.PermissionRepositoryImpl
import com.martorell.albert.meteomartocompose.data.server.APIKeyInterceptor
import com.martorell.albert.meteomartocompose.framework.db.MeteoMartoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApplicationScope

@Module
@InstallIn(SingletonComponent::class)
object AppProvideModule {

    private const val ROOM_DATABASE = "MeteoMartoDatabase"

    @Provides
    @Singleton
    fun providesDatabase(app: Application): MeteoMartoDatabase = Room.databaseBuilder(
        app,
        MeteoMartoDatabase::class.java,
        ROOM_DATABASE
    ).fallbackToDestructiveMigration(true)
        .build()

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun providesApiKeyInterceptor(): APIKeyInterceptor = APIKeyInterceptor()

    @Singleton
    @Provides
    fun providesOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        apiKeyInterceptor: APIKeyInterceptor
    ): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(apiKeyInterceptor)
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BuildConfig.API_WB_BASE)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun providesCityWeatherServerDataSource(retrofit: Retrofit): CityWeatherServerDataSource =
        retrofit.create(CityWeatherServerDataSource::class.java)

    @Provides
    @Singleton
    @ApplicationScope
    fun provideApplicationScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
}
