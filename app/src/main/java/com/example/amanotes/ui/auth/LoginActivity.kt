package com.example.amanotes.ui.auth

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.amanotes.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels { LoginViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            viewModel.login(
                binding.inputEmail.text?.toString()?.trim().orEmpty(),
                binding.inputPassword.text?.toString()?.trim().orEmpty()
            )
        }

        viewModel.loading.observe(this) { isLoading ->
            binding.progress.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnLogin.isEnabled = !isLoading
        }
        viewModel.error.observe(this) { message ->
            binding.tvError.visibility = if (message.isNullOrBlank()) View.GONE else View.VISIBLE
            binding.tvError.text = message ?: ""
        }
        viewModel.success.observe(this) { ok ->
            if (ok == true) {
                finish()
            }
        }
    }
}


