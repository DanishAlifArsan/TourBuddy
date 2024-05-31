package com.tourbuddy.ui.Main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tourbuddy.api.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}