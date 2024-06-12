package com.tourbuddy.ui.Main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tourbuddy.api.DestinationRepository
import com.tourbuddy.di.Injection

class DestinationViewModelFactory (private val repository: DestinationRepository) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: DestinationViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): DestinationViewModelFactory {
            if (INSTANCE == null) {
                synchronized(DestinationViewModelFactory::class.java) {
                    INSTANCE = DestinationViewModelFactory(Injection.provideDestinationRepository(context))
                }
            }
            return INSTANCE as DestinationViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DestinationViewModel::class.java)) {
            return DestinationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}