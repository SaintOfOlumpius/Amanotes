package com.example.amanotes.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amanotes.data.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    private val _success = MutableLiveData(false)
    val success: LiveData<Boolean> = _success

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _error.value = "Please fill all fields"
            return
        }
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            val result = repository.login(email, password)
            _loading.value = false
            result.onSuccess { _success.value = true }
                .onFailure { _error.value = it.message ?: "Login failed" }
        }
    }
}


