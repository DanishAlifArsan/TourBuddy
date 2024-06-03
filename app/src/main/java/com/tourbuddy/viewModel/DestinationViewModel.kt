package com.tourbuddy.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tourbuddy.api.DestinationResponse

class DestinationViewModel(val repository: DestinationRepository) : ViewModel() {
    val isLoading : LiveData<Boolean> = repository._isLoading

    fun getAllDestination(city : String) : LiveData<DestinationResponse> {
        return repository.getAllDestination(city)
    }
}