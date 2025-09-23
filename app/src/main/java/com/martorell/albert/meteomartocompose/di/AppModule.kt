package com.martorell.albert.meteomartocompose.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.martorell.albert.meteomartocompose.data.city.CityWeatherServerDataSource
import com.martorell.albert.meteomartocompose.data.city.repositories.PermissionChecker
import com.martorell.albert.meteomartocompose.data.city.repositories.PermissionRepository
import com.martorell.albert.meteomartocompose.data.cityweather.PermissionRepositoryImpl
import com.martorell.albert.meteomartocompose.data.server.APIKeyInterceptor
import com.martorell.albert.meteomartocompose.framework.db.MeteoMartoDatabase
import com.martorell.albert.meteomartocompose.utils.AndroidPermissionChecker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    companion object {
        private const val ROOM_DATABASE = "MeteoMartoDatabase"
        private const val BASE_URL = "https://api.openweathermap.org/"
    }

    @Provides
    @Singleton
    fun providesDatabase(app: Application): MeteoMartoDatabase = Room.databaseBuilder(
        app,
        MeteoMartoDatabase::class.java,
        ROOM_DATABASE
    ).fallbackToDestructiveMigration(true)
        .build() //fallbackToDestructiveMigration: each time there is an increase version we enable a destructive migration (database is cleared)

    @Provides
    fun providesPermissionRepository(
        permissionChecker: PermissionChecker
    ): PermissionRepository {
        return PermissionRepositoryImpl(permissionChecker)
    }

    @Provides
    fun providesPermissionChecker(app: Application): PermissionChecker =
        AndroidPermissionChecker(app)

    @Provides
    fun provideFusedLocationProviderClient(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun providesApiKeyInterceptor() = APIKeyInterceptor()

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
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun providesCityWeatherServerDataSource(retrofit: Retrofit): CityWeatherServerDataSource =
        retrofit.create(CityWeatherServerDataSource::class.java)

}