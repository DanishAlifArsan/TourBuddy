package com.tourbuddy.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tourbuddy.databinding.ActivityOnboardingBinding
import com.tourbuddy.ui.login.LoginActivity

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnStart.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}