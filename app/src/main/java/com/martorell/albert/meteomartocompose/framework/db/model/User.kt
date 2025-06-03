package com.martorell.albert.meteomartocompose.framework.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val uid: String = "",
    val email: String?,
    val name: String?
)