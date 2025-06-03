package com.martorell.albert.meteomartocompose.di

import android.app.Application
import androidx.room.Room
import com.martorell.albert.meteomartocompose.data.auth.RoomDataSource
import com.martorell.albert.meteomartocompose.data.auth.sources.LocalDataSource
import com.martorell.albert.meteomartocompose.framework.db.MeteoMartoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    fun databaseProvider(app: Application): MeteoMartoDatabase = Room.databaseBuilder(
        app,
        MeteoMartoDatabase::class.java,
        ROOM_DATABASE
    ).fallbackToDestructiveMigration(true)
        .build() //fallbackToDestructiveMigration: each time there is an increase version we enable a destructive migration (database is cleared)

    @Provides
    fun localDataSourceProvider(db: MeteoMartoDatabase): LocalDataSource = RoomDataSource(db)

}