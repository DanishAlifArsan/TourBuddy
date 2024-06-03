package com.tourbuddy.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    //opsi 1 meggunakan callback
//    @GET("destinations")
//    fun getAllDestinations(
//        @Query("city") city: String
//    ): Call<DestinationResponse>

    //opsi 2 menggunakan suspend function
    @GET("destinations")
    suspend fun getAllDestinations(
        @Query("city") city: String
    ): DestinationResponse

        @GET("reviews")
    fun getAllReview(
        @Query("id") id: String
    ): Call<ListReviewResponse>
}