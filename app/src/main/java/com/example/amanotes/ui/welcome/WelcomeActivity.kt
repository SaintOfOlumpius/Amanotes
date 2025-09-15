package com.example.amanotes.ui.welcome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.amanotes.databinding.ActivityWelcomeBinding
import com.example.amanotes.ui.auth.LoginActivity
import com.example.amanotes.ui.auth.SignupActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSlider()
    }

    private fun setupSlider() {
        // SeekBar centered at 50. Slide left (<50) → Login, right (>50) → Signup
        val center = 50
        binding.directionSlider.progress = center
        binding.directionSlider.max = 100

        binding.directionSlider.setOnSeekBarChangeListener(object : android.widget.SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: android.widget.SeekBar?, progress: Int, fromUser: Boolean) {
                if (!fromUser) return
                if (progress <= 5) {
                    navigateToLogin()
                } else if (progress >= 95) {
                    navigateToSignup()
                }
            }

            override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {
                binding.directionSlider.progress = center
            }
        })
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }

    private fun navigateToSignup() {
        startActivity(Intent(this, SignupActivity::class.java))
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }
}


