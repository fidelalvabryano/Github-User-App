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