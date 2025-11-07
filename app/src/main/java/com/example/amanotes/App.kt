package com.example.amanotes

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.amanotes.data.local.UserPreferences
import com.example.amanotes.utils.LocaleManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        
        // Apply saved language preference on app startup
        applySavedLanguage()
    }
    
    private fun applySavedLanguage() {
        // Run in a coroutine to read the saved language preference
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val userPreferences = UserPreferences(this@App)
                val languageCode = userPreferences.language.first()
                val language = LocaleManager.Language.fromCode(languageCode)
                
                // Apply the locale to the app
                LocaleManager.updateAppLocale(this@App, language)
            } catch (e: Exception) {
                // If there's an error, default to English
                LocaleManager.updateAppLocale(this@App, LocaleManager.Language.ENGLISH)
            }
        }
    }
    
    override fun attachBaseContext(base: android.content.Context) {
        // Apply locale before super.attachBaseContext for proper context wrapping
        // This ensures the locale is applied from the very beginning
        try {
            val languageCode = LocaleManager.getSavedLanguageCode(base)
            val language = LocaleManager.Language.fromCode(languageCode)
            val wrappedContext = LocaleManager.setLocale(base, language)
            super.attachBaseContext(wrappedContext)
        } catch (e: Exception) {
            // If there's any error, just use the base context with default locale
            super.attachBaseContext(base)
        }
    }
}
