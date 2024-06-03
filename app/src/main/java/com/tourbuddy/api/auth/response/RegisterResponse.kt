package com.tourbuddy.api.auth.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)
