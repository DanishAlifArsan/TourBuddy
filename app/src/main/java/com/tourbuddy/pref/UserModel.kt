package com.tourbuddy.pref

data class UserModel(
    val token: String,
    val email: String,
    val isLogin: Boolean = false
)