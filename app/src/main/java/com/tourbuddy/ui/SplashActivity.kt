package com.tourbuddy.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.tourbuddy.R

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            goToMain()
        }, 3000L)
    }

    private fun goToMain(){
        Intent(this, OnboardingActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }
}