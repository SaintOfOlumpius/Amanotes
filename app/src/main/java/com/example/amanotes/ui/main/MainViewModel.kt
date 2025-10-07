package com.example.amanotes.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amanotes.data.local.UserPreferences
import com.example.amanotes.data.repository.AuthRepository
import com.example.amanotes.ui.compose.theme.AppTheme
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()
    
    // Theme state from preferences
    val darkMode = userPreferences.darkMode
    
    val appTheme: StateFlow<AppTheme> = userPreferences.appTheme
        .map { themeString ->
            try {
                AppTheme.valueOf(themeString)
            } catch (e: IllegalArgumentException) {
                AppTheme.DARK_ACADEMIA
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppTheme.DARK_ACADEMIA
        )
    
    // Authentication state
    private val _isAuthenticated = MutableStateFlow<Boolean?>(null)
    val isAuthenticated: StateFlow<Boolean?> = _isAuthenticated.asStateFlow()
    
    init {
        checkAuthenticationStatus()
    }
    
    private fun checkAuthenticationStatus() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                val currentUser = authRepository.getCurrentUser()
                _isAuthenticated.value = currentUser != null
                
                // Update profile preferences if user exists but preferences are empty
                if (currentUser != null) {
                    val profileName = userPreferences.profileName.first()
                    if (profileName.isEmpty()) {
                        userPreferences.updateProfile(
                            name = currentUser.name,
                            title = "Scholar",
                            email = currentUser.email,
                            bio = "Passionate about learning and knowledge discovery."
                        )
                    }
                }
            } catch (e: Exception) {
                _isAuthenticated.value = false
                _uiState.value = _uiState.value.copy(
                    error = "Failed to check authentication: ${e.message}"
                )
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
    
    fun toggleDarkMode() {
        viewModelScope.launch {
            val current = darkMode.first()
            userPreferences.updateDarkMode(!current)
        }
    }
    
    fun updateAppTheme(theme: AppTheme) {
        viewModelScope.launch {
            userPreferences.updateAppTheme(theme.name)
        }
    }
    
    fun onAuthenticationSuccess() {
        _isAuthenticated.value = true
        checkAuthenticationStatus() // Refresh user data
    }
    
    fun onLogout() {
        _isAuthenticated.value = false
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class MainUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)
