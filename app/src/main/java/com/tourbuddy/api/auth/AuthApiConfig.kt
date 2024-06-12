package com.tourbuddy.api.auth

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AuthApiConfig {
    fun getApiService(): AuthApiService {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
//        val authInterceptor = Interceptor { chain ->
//            val req = chain.request()
//            val requestHeaders = req.newBuilder()
//                .addHeader("Authorization", "Bearer $token")
//                .build()
//            chain.proceed(requestHeaders)
//        }
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            //.addInterceptor(authInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://auth-service-jcoc7wc2ka-et.a.run.app/api/auth/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(AuthApiService::class.java)
    }
}