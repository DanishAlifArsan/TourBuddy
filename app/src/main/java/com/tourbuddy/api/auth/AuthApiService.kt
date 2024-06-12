package com.tourbuddy.api.auth

import com.tourbuddy.api.response.LoginResponse
import com.tourbuddy.api.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthApiService {
        @FormUrlEncoded
        @POST("signup")
        suspend fun register(
            @Field("username") name: String,
            @Field("email") email: String,
            @Field("password") password: String
        ): RegisterResponse

        @FormUrlEncoded
        @POST("login")
        suspend fun login(
            @Field("email") email: String,
            @Field("password") password: String
        ): LoginResponse
}