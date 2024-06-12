package com.tourbuddy.ui.Main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tourbuddy.api.DestinationRepository
import com.tourbuddy.api.response.DestinationResponse

class DestinationViewModel(val repository: DestinationRepository) : ViewModel() {
    val isLoading : LiveData<Boolean> = repository._isLoading

    fun getAllDestination(lat : Float, lon : Float) : LiveData<DestinationResponse> {
        return repository.getAllDestination(lat, lon)
    }
}