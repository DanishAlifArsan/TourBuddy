package com.tourbuddy.ui.Review

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tourbuddy.api.ReviewRepository
import com.tourbuddy.api.response.ListReviewResponse

class ListReviewViewModel(val repository: ReviewRepository) : ViewModel() {
    val isLoading : LiveData<Boolean> = repository._isLoading

    fun getAllReview(destination_id: String?) : LiveData<ListReviewResponse> {
        return repository.getAllReview(destination_id)
    }
}