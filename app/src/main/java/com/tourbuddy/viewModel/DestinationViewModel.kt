package com.tourbuddy.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.tourbuddy.api.ApiConfig
import com.tourbuddy.api.ApiService
import com.tourbuddy.api.DestinationResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class DestinationViewModel(val repository: DestinationRepository) : ViewModel() {
    val isLoading : LiveData<Boolean> = repository._isLoading

//    init {
//        getAllDestination("Indonesia")
//    }

    //opsi 1 menggunakan callback
//    fun getAllDestination(city : String) {
//        _isLoading.value = true
//        val client = ApiConfig.getApiService(token).getAllDestinations(city)
//        client.enqueue(object : Callback<DestinationResponse> {
//            override fun onResponse(
//                call: Call<DestinationResponse>,
//                response: Response<DestinationResponse>
//            ) {
//                _isLoading.value = false
//                if (response.isSuccessful) {
//                    _destination.value = response.body()
//                    Log.d("TAG", "onResponse: success")
//                } else {
//                    Log.d("TAG", "onResponse: failed")
//                }
//            }
//
//            override fun onFailure(call: Call<DestinationResponse>, t: Throwable) {
//                _isLoading.value = false
//                Log.d("TAG", "onResponse: error")
//            }
//
//        })
//    }

    //opsi 2 menggunakan suspend function
    fun getAllDestination(city : String) : LiveData<DestinationResponse> {
        return repository.getAllDestination(city)
    }
}