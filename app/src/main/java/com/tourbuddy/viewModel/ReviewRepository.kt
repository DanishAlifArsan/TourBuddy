package com.tourbuddy.viewModel

import android.media.Rating
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.tourbuddy.api.ApiConfig
import com.tourbuddy.api.ApiService
import com.tourbuddy.api.ListReviewResponse
import com.tourbuddy.api.ResultState
import com.tourbuddy.api.auth.response.AddReviewResponse
import com.tourbuddy.data.Review
import com.tourbuddy.pref.UserPreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException

class ReviewRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService,
    private val scope : CoroutineScope
) {
    val _isLoading = MutableLiveData<Boolean>()

    fun getAllReview(destination_id: String?) : MutableLiveData<ListReviewResponse> {
        val _review = MutableLiveData<ListReviewResponse>()
        _isLoading.value = true
        scope.launch {
            try {
                val user = runBlocking { userPreference.getSession().first() }
                val token = user.token
                _review.postValue(apiService.getAllReview("Bearer $token", destination_id))
                _isLoading.postValue(false)
                Log.d("TAG", "getAllReview: success")
            } catch (e : HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ListReviewResponse::class.java)
                Log.d("TAG", "getAllReview: $errorBody")
                _isLoading.postValue(false)
            }
        }
        return _review
    }

    fun addReview(review: String, rating: Int, destination_id: String?,) = liveData {
        emit(ResultState.Loading)
        try {
            val user = runBlocking { userPreference.getSession().first() }
            val token = user.token
            val successResponse = ApiConfig.getApiService().addReview("Bearer $token", review, rating, destination_id)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, AddReviewResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    companion object {
        @Volatile
        private var instance: ReviewRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService,
            scope : CoroutineScope
        ): ReviewRepository =
            instance ?: synchronized(this) {
                instance ?: ReviewRepository(userPreference, apiService, scope)
            }.also { instance = it }
    }
}