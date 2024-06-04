package com.tourbuddy.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tourbuddy.api.ApiConfig
import com.tourbuddy.api.DestinationResponse
import com.tourbuddy.api.ListReviewResponse
import kotlinx.coroutines.CoroutineScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListReviewViewModel(val repository: ReviewRepository) : ViewModel() {
    val isLoading : LiveData<Boolean> = repository._isLoading

    fun getAllReview(destination_id: String?) : LiveData<ListReviewResponse> {
        return repository.getAllReview(destination_id)
    }
}