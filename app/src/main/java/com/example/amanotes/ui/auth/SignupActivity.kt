package com.example.amanotes.ui.auth

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.amanotes.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val viewModel: SignupViewModel by viewModels { SignupViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignup.setOnClickListener {
            viewModel.signup(
                binding.inputName.text?.toString()?.trim().orEmpty(),
                binding.inputEmail.text?.toString()?.trim().orEmpty(),
                binding.inputPassword.text?.toString()?.trim().orEmpty()
            )
        }

        viewModel.loading.observe(this) { isLoading ->
            binding.progress.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnSignup.isEnabled = !isLoading
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


