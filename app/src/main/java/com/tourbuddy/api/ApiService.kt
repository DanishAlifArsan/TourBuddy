package com.tourbuddy.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("destinations")
    suspend fun getAllDestinations(
        @Query("city") city: String
    ): DestinationResponse

        @GET("reviews")
    fun getAllReview(
        @Query("id") id: String
    ): Call<ListReviewResponse>
}