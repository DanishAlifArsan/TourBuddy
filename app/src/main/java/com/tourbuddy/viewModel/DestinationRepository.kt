package com.tourbuddy.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tourbuddy.api.ApiService
import com.tourbuddy.api.DestinationResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DestinationRepository private constructor(
    private val apiService: ApiService,
    private val scope : CoroutineScope
) {
    val _isLoading = MutableLiveData<Boolean>()

    fun getAllDestination(city : String) : MutableLiveData<DestinationResponse> {
        val _destination = MutableLiveData<DestinationResponse>()
        _isLoading.value = true
        scope.launch {
            try {
                _destination.postValue(apiService.getAllDestinations(city))
                _isLoading.postValue(false)
                Log.d("TAG", "getAllDestination: success")
            } catch (e : HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, DestinationResponse::class.java)
                Log.d("TAG", "getAllDestination: $errorBody")
                _isLoading.postValue(false)
            }
        }
        return _destination
    }

    companion object {
        @Volatile
        private var instance: DestinationRepository? = null
        fun getInstance(
            apiService: ApiService,
            scope : CoroutineScope
        ): DestinationRepository =
            instance ?: synchronized(this) {
                instance ?: DestinationRepository(apiService, scope)
            }.also { instance = it }
    }
}