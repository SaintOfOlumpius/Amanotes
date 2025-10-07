package com.example.amanotes.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferences(private val context: Context) {
    
    companion object {
        // Theme preferences
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val APP_THEME = stringPreferencesKey("app_theme")
        
        // Notification preferences
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val VIBRATION_ENABLED = booleanPreferencesKey("vibration_enabled")
        val NOTIFICATION_FREQUENCY = stringPreferencesKey("notification_frequency")
        
        // Sync preferences
        val AUTO_SYNC = booleanPreferencesKey("auto_sync")
        val SYNC_FREQUENCY = stringPreferencesKey("sync_frequency")
        val WIFI_ONLY_SYNC = booleanPreferencesKey("wifi_only_sync")
        
        // Privacy preferences
        val ANALYTICS_ENABLED = booleanPreferencesKey("analytics_enabled")
        val CRASH_REPORTING = booleanPreferencesKey("crash_reporting")
        val PERSONALIZED_ADS = booleanPreferencesKey("personalized_ads")
        
        // Security preferences
        val BIOMETRIC_AUTH = booleanPreferencesKey("biometric_auth")
        val AUTO_LOCK_TIMEOUT = intPreferencesKey("auto_lock_timeout")
        val REQUIRE_AUTH_FOR_SENSITIVE = booleanPreferencesKey("require_auth_sensitive")
        
        // App preferences
        val LANGUAGE = stringPreferencesKey("language")
        val FONT_SIZE = stringPreferencesKey("font_size")
        val AUTO_BACKUP = booleanPreferencesKey("auto_backup")
        val BACKUP_FREQUENCY = stringPreferencesKey("backup_frequency")
        
        // Profile preferences
        val PROFILE_NAME = stringPreferencesKey("profile_name")
        val PROFILE_TITLE = stringPreferencesKey("profile_title")
        val PROFILE_EMAIL = stringPreferencesKey("profile_email")
        val PROFILE_BIO = stringPreferencesKey("profile_bio")
    }
    
    // Theme preferences
    val darkMode: Flow<Boolean> = context.dataStore.data.map { it[DARK_MODE] ?: false }
    val appTheme: Flow<String> = context.dataStore.data.map { it[APP_THEME] ?: "DARK_ACADEMIA" }
    
    // Notification preferences
    val notificationsEnabled: Flow<Boolean> = context.dataStore.data.map { it[NOTIFICATIONS_ENABLED] ?: true }
    val soundEnabled: Flow<Boolean> = context.dataStore.data.map { it[SOUND_ENABLED] ?: true }
    val vibrationEnabled: Flow<Boolean> = context.dataStore.data.map { it[VIBRATION_ENABLED] ?: true }
    val notificationFrequency: Flow<String> = context.dataStore.data.map { it[NOTIFICATION_FREQUENCY] ?: "normal" }
    
    // Sync preferences
    val autoSync: Flow<Boolean> = context.dataStore.data.map { it[AUTO_SYNC] ?: true }
    val syncFrequency: Flow<String> = context.dataStore.data.map { it[SYNC_FREQUENCY] ?: "hourly" }
    val wifiOnlySync: Flow<Boolean> = context.dataStore.data.map { it[WIFI_ONLY_SYNC] ?: false }
    
    // Privacy preferences
    val analyticsEnabled: Flow<Boolean> = context.dataStore.data.map { it[ANALYTICS_ENABLED] ?: true }
    val crashReporting: Flow<Boolean> = context.dataStore.data.map { it[CRASH_REPORTING] ?: true }
    val personalizedAds: Flow<Boolean> = context.dataStore.data.map { it[PERSONALIZED_ADS] ?: false }
    
    // Security preferences
    val biometricAuth: Flow<Boolean> = context.dataStore.data.map { it[BIOMETRIC_AUTH] ?: false }
    val autoLockTimeout: Flow<Int> = context.dataStore.data.map { it[AUTO_LOCK_TIMEOUT] ?: 5 }
    val requireAuthForSensitive: Flow<Boolean> = context.dataStore.data.map { it[REQUIRE_AUTH_FOR_SENSITIVE] ?: false }
    
    // App preferences
    val language: Flow<String> = context.dataStore.data.map { it[LANGUAGE] ?: "en" }
    val fontSize: Flow<String> = context.dataStore.data.map { it[FONT_SIZE] ?: "medium" }
    val autoBackup: Flow<Boolean> = context.dataStore.data.map { it[AUTO_BACKUP] ?: true }
    val backupFrequency: Flow<String> = context.dataStore.data.map { it[BACKUP_FREQUENCY] ?: "daily" }
    
    // Profile preferences
    val profileName: Flow<String> = context.dataStore.data.map { it[PROFILE_NAME] ?: "" }
    val profileTitle: Flow<String> = context.dataStore.data.map { it[PROFILE_TITLE] ?: "" }
    val profileEmail: Flow<String> = context.dataStore.data.map { it[PROFILE_EMAIL] ?: "" }
    val profileBio: Flow<String> = context.dataStore.data.map { it[PROFILE_BIO] ?: "" }
    
    // Update functions
    suspend fun updateDarkMode(enabled: Boolean) {
        context.dataStore.edit { it[DARK_MODE] = enabled }
    }
    
    suspend fun updateAppTheme(theme: String) {
        context.dataStore.edit { it[APP_THEME] = theme }
    }
    
    suspend fun updateNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { it[NOTIFICATIONS_ENABLED] = enabled }
    }
    
    suspend fun updateSoundEnabled(enabled: Boolean) {
        context.dataStore.edit { it[SOUND_ENABLED] = enabled }
    }
    
    suspend fun updateVibrationEnabled(enabled: Boolean) {
        context.dataStore.edit { it[VIBRATION_ENABLED] = enabled }
    }
    
    suspend fun updateNotificationFrequency(frequency: String) {
        context.dataStore.edit { it[NOTIFICATION_FREQUENCY] = frequency }
    }
    
    suspend fun updateAutoSync(enabled: Boolean) {
        context.dataStore.edit { it[AUTO_SYNC] = enabled }
    }
    
    suspend fun updateSyncFrequency(frequency: String) {
        context.dataStore.edit { it[SYNC_FREQUENCY] = frequency }
    }
    
    suspend fun updateWifiOnlySync(enabled: Boolean) {
        context.dataStore.edit { it[WIFI_ONLY_SYNC] = enabled }
    }
    
    suspend fun updateAnalyticsEnabled(enabled: Boolean) {
        context.dataStore.edit { it[ANALYTICS_ENABLED] = enabled }
    }
    
    suspend fun updateCrashReporting(enabled: Boolean) {
        context.dataStore.edit { it[CRASH_REPORTING] = enabled }
    }
    
    suspend fun updatePersonalizedAds(enabled: Boolean) {
        context.dataStore.edit { it[PERSONALIZED_ADS] = enabled }
    }
    
    suspend fun updateBiometricAuth(enabled: Boolean) {
        context.dataStore.edit { it[BIOMETRIC_AUTH] = enabled }
    }
    
    suspend fun updateAutoLockTimeout(minutes: Int) {
        context.dataStore.edit { it[AUTO_LOCK_TIMEOUT] = minutes }
    }
    
    suspend fun updateRequireAuthForSensitive(enabled: Boolean) {
        context.dataStore.edit { it[REQUIRE_AUTH_FOR_SENSITIVE] = enabled }
    }
    
    suspend fun updateLanguage(language: String) {
        context.dataStore.edit { it[LANGUAGE] = language }
    }
    
    suspend fun updateFontSize(size: String) {
        context.dataStore.edit { it[FONT_SIZE] = size }
    }
    
    suspend fun updateAutoBackup(enabled: Boolean) {
        context.dataStore.edit { it[AUTO_BACKUP] = enabled }
    }
    
    suspend fun updateBackupFrequency(frequency: String) {
        context.dataStore.edit { it[BACKUP_FREQUENCY] = frequency }
    }
    
    suspend fun updateProfile(name: String, title: String, email: String, bio: String) {
        context.dataStore.edit { preferences ->
            preferences[PROFILE_NAME] = name
            preferences[PROFILE_TITLE] = title
            preferences[PROFILE_EMAIL] = email
            preferences[PROFILE_BIO] = bio
        }
    }
}
