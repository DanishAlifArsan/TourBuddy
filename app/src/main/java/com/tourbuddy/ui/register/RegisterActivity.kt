package com.tourbuddy.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.tourbuddy.ui.login.LoginActivity
import com.tourbuddy.ui.Main.MainActivity
import com.tourbuddy.api.ResultState
import com.tourbuddy.databinding.ActivityRegisterBinding
import com.tourbuddy.ui.OnboardingActivity
import com.tourbuddy.viewModelFactory.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
    }

    private fun setupAction() {
        binding.btnSingin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnBack.setOnClickListener{
            finish()
        }

        binding.btnSignup.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            if (name == ""){
                showToast("Name must be filled")
            }
            else if (email == "") {
                showToast("Email must be filled")
            }
            else if (password == "") {
                showToast("Password must be filled")
            }
            else {
                viewModel.register(name, email, password).observe(this) { result ->
                    if (result != null) {
                        when (result) {
                            is ResultState.Loading -> {
                            }

                            is ResultState.Success -> {
//                            showToast("Akun anda sudah jadi")
                                val intent = Intent(this, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                            }

                            is ResultState.Error -> {
                                showToast(result.error)
                            }
                        }
                    }
                }
            }

            binding.btnBack.setOnClickListener {
                startActivity(Intent(this, LoginActivity::class.java))
            }

        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}