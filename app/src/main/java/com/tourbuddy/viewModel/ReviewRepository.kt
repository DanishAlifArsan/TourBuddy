package com.tourbuddy.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tourbuddy.api.ApiService
import com.tourbuddy.api.ListReviewResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ReviewRepository private constructor(
    private val apiService: ApiService,
    private val scope : CoroutineScope
) {
    val _isLoading = MutableLiveData<Boolean>()

    fun getAllReview(destination_id: String?) : MutableLiveData<ListReviewResponse> {
        val _review = MutableLiveData<ListReviewResponse>()
        _isLoading.value = true
        scope.launch {
            try {
                _review.postValue(apiService.getAllReview(destination_id))
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
            apiService: ApiService,
            scope : CoroutineScope
        ): ReviewRepository =
            instance ?: synchronized(this) {
                instance ?: ReviewRepository(apiService, scope)
            }.also { instance = it }
    }
}