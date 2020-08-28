package com.mhamza007.iptv.api

import okhttp3.Interceptor
import okhttp3.Response

class ActivationCodeAuthInterceptor(private val activationCode: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val authenticatedRequest = request.newBuilder().header("Authorization", activationCode).build()
        return chain.proceed(authenticatedRequest)
    }
}