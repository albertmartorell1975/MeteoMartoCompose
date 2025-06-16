package com.martorell.albert.meteomartocompose.framework.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocationLocal(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val latitude: Double? = 0.0,
    val longitude: Double? = 0.0,
)