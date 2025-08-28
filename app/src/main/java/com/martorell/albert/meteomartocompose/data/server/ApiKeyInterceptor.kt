package com.martorell.albert.meteomartocompose.data.server

import okhttp3.Interceptor
import okhttp3.Response

class APIKeyInterceptor : Interceptor {

    companion object {
        private const val APP_ID_PARAM = "appid"
        private const val API_KEY_VALUE = "a32162b65432dca541a0e24d41cc4c7a"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val currentUrl = chain.request().url
        val newUrl = currentUrl.newBuilder()
            .addQueryParameter(APP_ID_PARAM, API_KEY_VALUE).build()
        val currentRequest = chain.request().newBuilder()
        val newRequest = currentRequest.url(newUrl).build()
        return chain.proceed(newRequest)
    }
}