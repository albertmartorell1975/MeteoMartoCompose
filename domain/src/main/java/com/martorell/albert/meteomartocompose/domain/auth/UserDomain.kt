package com.martorell.albert.meteomartocompose.domain.auth

data class UserDomain(
    val uid: String = "",
    val email: String?,
    val name: String?
)