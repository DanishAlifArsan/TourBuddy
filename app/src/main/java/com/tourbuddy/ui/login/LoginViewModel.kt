package com.tourbuddy.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.tourbuddy.api.UserRepository
import com.tourbuddy.pref.UserModel

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
    fun login(email: String, password: String) = repository.login(email, password)
}