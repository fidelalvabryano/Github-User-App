package com.fidel.githubuserappfinal.api

import okhttp3.Interceptor
import okhttp3.Response

class MyInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", "token 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")
            .build()
        return chain.proceed(request)
    }
}