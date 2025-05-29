package com.martorell.albert.meteomartocompose.data

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import java.io.IOException

typealias Result<T> = Either<CustomError, T>

sealed class CustomError {

    class FirebaseError(val code: Int) : CustomError()
    data object Connectivity : CustomError()
    class Unknown(val message: String) : CustomError()
}

fun Exception.toCustomError(): CustomError =

    when (this) {
        is IOException -> CustomError.Connectivity
        //is HttpException -> CustomError.FirebaseError(code())
        else -> CustomError.Unknown(message ?: "")
    }


inline fun <T> customTryCatch(action: () -> T): Result<T> = try {
    action().right()
} catch (ex: Exception) {
    ex.toCustomError().left()
}