package com.example.amanotes.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amanotes.data.repository.AuthRepository
import kotlinx.coroutines.launch

class SignupViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    private val _success = MutableLiveData(false)
    val success: LiveData<Boolean> = _success

    fun signup(name: String, email: String, password: String) {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            _error.value = "Please fill all fields"
            return
        }
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            val result = repository.signup(name, email, password)
            _loading.value = false
            result.onSuccess { _success.value = true }
                .onFailure { _error.value = it.message ?: "Sign up failed" }
        }
    }
}


