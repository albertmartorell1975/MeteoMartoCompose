package com.martorell.albert.meteomartocompose.data

import retrofit2.HttpException
import java.io.IOException

/**
 * This custom error class is meant for Flows approach
 */
sealed class CustomErrorFlow {

    class FirebaseError(val code: Int) : CustomErrorFlow()
    data object Connectivity : CustomErrorFlow()
    class Unknown(val message: String) : CustomErrorFlow()
}

fun Throwable.toCustomErrorFlow(): CustomErrorFlow =

    when (this) {

        is IOException ->
            CustomErrorFlow.Connectivity

        is HttpException ->
            CustomErrorFlow.FirebaseError(code())

        else ->
            CustomErrorFlow.Unknown(message ?: "")
    }


/**
 * If everything works fine it will return null else the customErrorFlow
 */
inline fun <T> customFlowTryCatch(action: () -> T): CustomErrorFlow? = try {
    action()
    null
} catch (ex: Exception) {
    ex.toCustomErrorFlow()
}