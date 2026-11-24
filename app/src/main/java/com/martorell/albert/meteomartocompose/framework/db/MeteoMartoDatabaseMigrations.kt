package com.martorell.albert.meteomartocompose.framework.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Non-destructive Room migration from schema version 2 to version 3.
 *
 * Appends `weatherConditionId` and `isNight` columns (`INTEGER NOT NULL DEFAULT 0`)
 * to the `CityWeather` table while preserving all existing city, user, and location rows.
 */
val MIGRATION_2_3: Migration = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "ALTER TABLE CityWeather ADD COLUMN weatherConditionId INTEGER NOT NULL DEFAULT 0",
        )
        db.execSQL(
            "ALTER TABLE CityWeather ADD COLUMN isNight INTEGER NOT NULL DEFAULT 0",
        )
    }
}
