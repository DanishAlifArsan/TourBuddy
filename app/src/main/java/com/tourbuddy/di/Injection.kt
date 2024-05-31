package com.tourbuddy.di

import android.content.Context
import com.tourbuddy.api.UserRepository
import com.tourbuddy.api.auth.AuthApiConfig
import com.tourbuddy.pref.UserPreference
import com.tourbuddy.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = AuthApiConfig.getApiService()
        return UserRepository.getInstance(pref,apiService)
    }
}