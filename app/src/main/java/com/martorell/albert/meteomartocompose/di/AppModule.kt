package com.martorell.albert.meteomartocompose.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.martorell.albert.meteomartocompose.data.auth.RoomDataSource
import com.martorell.albert.meteomartocompose.data.auth.repositories.cityweather.PermissionChecker
import com.martorell.albert.meteomartocompose.data.auth.repositories.cityweather.PermissionRepository
import com.martorell.albert.meteomartocompose.data.auth.sources.auth.LocalDataSource
import com.martorell.albert.meteomartocompose.data.cityweather.PermissionRepositoryImpl
import com.martorell.albert.meteomartocompose.framework.db.MeteoMartoDatabase
import com.martorell.albert.meteomartocompose.utils.AndroidPermissionChecker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    companion object {
        private const val ROOM_DATABASE = "MeteoMartoDatabase"
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
    fun providesLocalDataSource(db: MeteoMartoDatabase): LocalDataSource = RoomDataSource(db)

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

}