package com.tourbuddy.viewModelFactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tourbuddy.api.ReviewRepository
import com.tourbuddy.di.Injection
import com.tourbuddy.ui.AddReview.AddReviewViewModel
import com.tourbuddy.ui.Review.ListReviewViewModel

class ReviewViewModelFactory (private val repository: ReviewRepository) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: ReviewViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ReviewViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ReviewViewModelFactory::class.java) {
                    INSTANCE = ReviewViewModelFactory(Injection.provideReviewRepository(context))
                }
            }
            return INSTANCE as ReviewViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListReviewViewModel::class.java)) {
            return ListReviewViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(AddReviewViewModel::class.java)) {
            return AddReviewViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}