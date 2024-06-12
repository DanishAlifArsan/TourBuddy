package com.tourbuddy.api

import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.tourbuddy.api.auth.AuthApiService
import com.tourbuddy.api.response.LoginResponse
import com.tourbuddy.api.response.RegisterResponse
import com.tourbuddy.pref.UserModel
import com.tourbuddy.pref.UserPreference
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: AuthApiService
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }


    suspend fun logout() {
        userPreference.logout()
    }

    fun register(name: String, email: String, password: String) = liveData {
        try {
            val successResponse = apiService.register(name, email, password)
            successResponse.token?.let { it ->
                UserModel(
                    it,
                    email,
                    true
                )
            }?.let {
                saveSession(
                    it
                )
            }
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            emit(errorResponse.message?.let { ResultState.Error(it) })
        }
    }

    fun login(email: String, password: String) = liveData {
        try {
            val successResponse = apiService.login(email, password)
            successResponse.loginResult?.token?.let { it ->
                UserModel(
                    it,
                    email,
                    true
                )
            }?.let {
                saveSession(
                    it
                )
            }
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(errorResponse.message?.let { ResultState.Error(it) })
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: AuthApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}