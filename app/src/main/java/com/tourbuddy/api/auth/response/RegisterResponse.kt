package com.tourbuddy.api.auth.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

    @field:SerializedName("registerResult")
    val registerResult: RegisterResult? = null,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class RegisterResult(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("userId")
    val userId: String? = null,

    @field:SerializedName("token")
    val token: String? = null
)