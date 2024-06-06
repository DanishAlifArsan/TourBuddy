package com.tourbuddy.viewModel

import androidx.lifecycle.ViewModel

class AddReviewViewModel(private val repository: ReviewRepository) : ViewModel() {

    fun addReview(review: String, rating: Int, destinationId: String?) = repository.addReview(destinationId, review, rating)
}