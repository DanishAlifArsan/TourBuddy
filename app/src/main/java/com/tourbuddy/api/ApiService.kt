package com.tourbuddy.api

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @GET("destination")
    suspend fun getAllDestinations(
        @Field("lat") lat: Float,
        @Field("lon") lon: Float
    ): DestinationResponse

        @GET("reviews")
    fun getAllReview(
        @Query("id") id: String
    ): Call<ListReviewResponse>
}