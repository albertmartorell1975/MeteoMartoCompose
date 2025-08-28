package com.martorell.albert.meteomartocompose.utils

import com.google.firebase.auth.FirebaseUser
import com.martorell.albert.meteomartocompose.domain.auth.UserDomain
import com.martorell.albert.meteomartocompose.framework.db.model.User

fun FirebaseUser.toDomain(): UserDomain =
    UserDomain(
        uid = this.uid,
        email = this.email,
        name = this.displayName
    )

fun UserDomain.toRoom(
): User =
    User(
        uid = this.uid,
        email = this.email,
        name = this.name
    )