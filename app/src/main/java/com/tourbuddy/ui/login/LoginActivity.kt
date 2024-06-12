package com.tourbuddy.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tourbuddy.ui.Main.MainActivity
import com.tourbuddy.ui.OnboardingActivity
import com.tourbuddy.api.ResultState
import com.tourbuddy.databinding.ActivityLoginBinding
import com.tourbuddy.ui.register.RegisterActivity
import com.tourbuddy.viewModelFactory.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
    }

    private fun setupAction() {
        binding.btnSignin.setOnClickListener{
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            if (email == ""){
                showToast("Email must be filled")
            }
            else if (password == "") {
                showToast("Password must be filled")
            }
            else {
                viewModel.login(email, password).observe(this) { result ->
                    if (result != null) {
                        when (result) {
                            is ResultState.Loading -> {
                            }

                            is ResultState.Success -> {
//                            showToast("Anda berhasil login")
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                finish()
                            }

                            is ResultState.Error -> {
                                showToast(result.error)
                            }
                        }
                    }
                }
            }
        }
        binding.btnSingup.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.btnBack.setOnClickListener{
            startActivity(Intent(this, OnboardingActivity::class.java))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    public override fun onStart() {
        super.onStart()
        viewModel.getSession().observe(this) { user ->
            if (user.isLogin) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}