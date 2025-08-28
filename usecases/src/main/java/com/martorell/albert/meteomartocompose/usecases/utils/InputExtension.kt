package com.martorell.albert.meteomartocompose.usecases.utils

fun String.isValidPassword(): Boolean = this.length > Constants.MINIMUM_PASSWORD_LENGTH

fun String.isFieldNotEmpty(): Boolean = this.isNotEmpty()