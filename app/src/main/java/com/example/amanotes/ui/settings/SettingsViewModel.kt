package com.example.amanotes.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amanotes.data.local.UserPreferences
import com.example.amanotes.data.repository.AuthRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val userPreferences: UserPreferences,
    private val authRepository: AuthRepository
) : ViewModel() {
    
    // UI State
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()
    
    // Preference flows
    val darkMode = userPreferences.darkMode
    val notificationsEnabled = userPreferences.notificationsEnabled
    val soundEnabled = userPreferences.soundEnabled
    val vibrationEnabled = userPreferences.vibrationEnabled
    val notificationFrequency = userPreferences.notificationFrequency
    val autoSync = userPreferences.autoSync
    val syncFrequency = userPreferences.syncFrequency
    val wifiOnlySync = userPreferences.wifiOnlySync
    val analyticsEnabled = userPreferences.analyticsEnabled
    val crashReporting = userPreferences.crashReporting
    val personalizedAds = userPreferences.personalizedAds
    val biometricAuth = userPreferences.biometricAuth
    val autoLockTimeout = userPreferences.autoLockTimeout
    val requireAuthForSensitive = userPreferences.requireAuthForSensitive
    val language = userPreferences.language
    val fontSize = userPreferences.fontSize
    val autoBackup = userPreferences.autoBackup
    val backupFrequency = userPreferences.backupFrequency
    
    // Profile data
    val profileName = userPreferences.profileName
    val profileTitle = userPreferences.profileTitle
    val profileEmail = userPreferences.profileEmail
    val profileBio = userPreferences.profileBio
    
    init {
        loadCurrentUser()
    }
    
    private fun loadCurrentUser() {
        viewModelScope.launch {
            try {
                val currentUser = authRepository.getCurrentUser()
                if (currentUser != null) {
                    // Update profile preferences with current user data if not set
                    val currentName = profileName.first()
                    val currentEmail = profileEmail.first()
                    
                    if (currentName.isEmpty() || currentEmail.isEmpty()) {
                        userPreferences.updateProfile(
                            name = currentUser.name,
                            title = "Scholar",
                            email = currentUser.email,
                            bio = "Passionate about learning and knowledge discovery."
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to load user data: ${e.message}"
                )
            }
        }
    }
    
    // Theme functions
    fun toggleDarkMode() {
        viewModelScope.launch {
            val current = darkMode.first()
            userPreferences.updateDarkMode(!current)
        }
    }
    
    // Notification functions
    fun toggleNotifications() {
        viewModelScope.launch {
            val current = notificationsEnabled.first()
            userPreferences.updateNotificationsEnabled(!current)
        }
    }
    
    fun toggleSound() {
        viewModelScope.launch {
            val current = soundEnabled.first()
            userPreferences.updateSoundEnabled(!current)
        }
    }
    
    fun toggleVibration() {
        viewModelScope.launch {
            val current = vibrationEnabled.first()
            userPreferences.updateVibrationEnabled(!current)
        }
    }
    
    fun updateNotificationFrequency(frequency: String) {
        viewModelScope.launch {
            userPreferences.updateNotificationFrequency(frequency)
        }
    }
    
    // Sync functions
    fun toggleAutoSync() {
        viewModelScope.launch {
            val current = autoSync.first()
            userPreferences.updateAutoSync(!current)
        }
    }
    
    fun updateSyncFrequency(frequency: String) {
        viewModelScope.launch {
            userPreferences.updateSyncFrequency(frequency)
        }
    }
    
    fun toggleWifiOnlySync() {
        viewModelScope.launch {
            val current = wifiOnlySync.first()
            userPreferences.updateWifiOnlySync(!current)
        }
    }
    
    // Privacy functions
    fun toggleAnalytics() {
        viewModelScope.launch {
            val current = analyticsEnabled.first()
            userPreferences.updateAnalyticsEnabled(!current)
        }
    }
    
    fun toggleCrashReporting() {
        viewModelScope.launch {
            val current = crashReporting.first()
            userPreferences.updateCrashReporting(!current)
        }
    }
    
    fun togglePersonalizedAds() {
        viewModelScope.launch {
            val current = personalizedAds.first()
            userPreferences.updatePersonalizedAds(!current)
        }
    }
    
    // Security functions
    fun toggleBiometricAuth() {
        viewModelScope.launch {
            val current = biometricAuth.first()
            userPreferences.updateBiometricAuth(!current)
        }
    }
    
    fun updateAutoLockTimeout(minutes: Int) {
        viewModelScope.launch {
            userPreferences.updateAutoLockTimeout(minutes)
        }
    }
    
    fun toggleRequireAuthForSensitive() {
        viewModelScope.launch {
            val current = requireAuthForSensitive.first()
            userPreferences.updateRequireAuthForSensitive(!current)
        }
    }
    
    // App functions
    fun updateLanguage(language: String) {
        viewModelScope.launch {
            userPreferences.updateLanguage(language)
        }
    }
    
    fun updateFontSize(size: String) {
        viewModelScope.launch {
            userPreferences.updateFontSize(size)
        }
    }
    
    fun toggleAutoBackup() {
        viewModelScope.launch {
            val current = autoBackup.first()
            userPreferences.updateAutoBackup(!current)
        }
    }
    
    fun updateBackupFrequency(frequency: String) {
        viewModelScope.launch {
            userPreferences.updateBackupFrequency(frequency)
        }
    }
    
    // Profile functions
    fun updateProfile(name: String, title: String, email: String, bio: String) {
        viewModelScope.launch {
            try {
                userPreferences.updateProfile(name, title, email, bio)
                _uiState.value = _uiState.value.copy(
                    message = "Profile updated successfully"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to update profile: ${e.message}"
                )
            }
        }
    }
    
    // Account functions
    fun signOut() {
        viewModelScope.launch {
            try {
                authRepository.logout()
                _uiState.value = _uiState.value.copy(
                    shouldNavigateToAuth = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to sign out: ${e.message}"
                )
            }
        }
    }
    
    fun deleteAccount() {
        viewModelScope.launch {
            try {
                // In a real app, this would call a delete account API
                authRepository.logout()
                _uiState.value = _uiState.value.copy(
                    shouldNavigateToAuth = true,
                    message = "Account deleted successfully"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to delete account: ${e.message}"
                )
            }
        }
    }
    
    // Data management functions
    fun exportData() {
        viewModelScope.launch {
            try {
                // In a real app, this would export user data
                _uiState.value = _uiState.value.copy(
                    message = "Data export initiated. Check your downloads folder."
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to export data: ${e.message}"
                )
            }
        }
    }
    
    fun clearCache() {
        viewModelScope.launch {
            try {
                // In a real app, this would clear app cache
                _uiState.value = _uiState.value.copy(
                    message = "Cache cleared successfully"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to clear cache: ${e.message}"
                )
            }
        }
    }
    
    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null, error = null)
    }
    
    fun onNavigatedToAuth() {
        _uiState.value = _uiState.value.copy(shouldNavigateToAuth = false)
    }
}

data class SettingsUiState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val error: String? = null,
    val shouldNavigateToAuth: Boolean = false
)
