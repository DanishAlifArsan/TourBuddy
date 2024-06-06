package com.tourbuddy.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tourbuddy.api.ApiService
import com.tourbuddy.api.ListReviewResponse
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
                _review.postValue(apiService.getAllReview(token, destination_id))
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