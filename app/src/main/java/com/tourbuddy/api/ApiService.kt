package com.tourbuddy.api

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {

    @GET("destination")
    suspend fun getAllDestinations(
        @Header("Authorization") token: String,
        @Query("lat") lat: Float,
        @Query("lon") lon: Float
    ): DestinationResponse

    @GET("review")
    suspend fun getAllReview(
        @Header("Authorization") token: String,
        @Query("destination_id") destination_id: String?,
    ): ListReviewResponse
}