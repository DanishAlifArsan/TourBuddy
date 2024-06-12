package com.tourbuddy.di

import android.content.Context
import com.tourbuddy.api.ApiConfig
import com.tourbuddy.api.UserRepository
import com.tourbuddy.api.auth.AuthApiConfig
import com.tourbuddy.pref.UserPreference
import com.tourbuddy.pref.dataStore
import com.tourbuddy.api.DestinationRepository
import com.tourbuddy.api.ReviewRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = AuthApiConfig.getApiService()
        return UserRepository.getInstance(pref,apiService)
    }

    fun provideDestinationRepository(context: Context) : DestinationRepository {
        val pref = UserPreference.getInstance(context.dataStore)
//        val token = runBlocking { pref.getSession().first().token }
        val apiService = ApiConfig.getApiService()
        return DestinationRepository.getInstance(pref, apiService, CoroutineScope(Dispatchers.IO))
    }

    fun provideReviewRepository(context: Context) : ReviewRepository {
        val pref = UserPreference.getInstance(context.dataStore)
//        val token = runBlocking { pref.getSession().first().token }
        val apiService = ApiConfig.getApiService()
        return ReviewRepository.getInstance(pref, apiService, CoroutineScope(Dispatchers.IO))
    }
}