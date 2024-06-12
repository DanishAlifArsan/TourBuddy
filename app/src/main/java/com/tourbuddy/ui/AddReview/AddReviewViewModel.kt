package com.tourbuddy.ui.AddReview

import androidx.lifecycle.ViewModel
import com.tourbuddy.api.ReviewRepository

class AddReviewViewModel(private val repository: ReviewRepository) : ViewModel() {

    fun addReview(review: String, rating: Int, destinationId: String?) = repository.addReview(review, rating, destinationId)
}