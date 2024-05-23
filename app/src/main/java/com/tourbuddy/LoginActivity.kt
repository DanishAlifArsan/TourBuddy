package com.tourbuddy

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tourbuddy.databinding.ActivityLoginBinding
import com.tourbuddy.databinding.ActivityOnboardingBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_login)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        binding.btnSignin.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }
        binding.btnSingup.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.btnBack.setOnClickListener{
            startActivity(Intent(this, OnboardingActivity::class.java))
        }
    }
}