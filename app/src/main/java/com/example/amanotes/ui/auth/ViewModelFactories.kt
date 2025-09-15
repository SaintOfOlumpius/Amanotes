package com.example.amanotes.ui.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.amanotes.data.repository.AuthRepository
import com.example.amanotes.di.ServiceLocator

class LoginViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = ServiceLocator.provideDatabase(context)
        val repo = AuthRepository(ServiceLocator.provideAuthService(), db.userDao())
        return LoginViewModel(repo) as T
    }
}

class SignupViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = ServiceLocator.provideDatabase(context)
        val repo = AuthRepository(ServiceLocator.provideAuthService(), db.userDao())
        return SignupViewModel(repo) as T
    }
}


