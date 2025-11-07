package com.example.amanotes.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.coroutines.flow.first
import java.util.Locale

object LocaleManager {
    
    // South African official languages
    enum class Language(val code: String, val displayName: String) {
        ENGLISH("en", "English"),
        AFRIKAANS("af", "Afrikaans"),
        ZULU("zu", "isiZulu"),
        XHOSA("xh", "isiXhosa"),
        SEPEDI("nso", "Sepedi"),
        SETSWANA("tn", "Setswana"),
        SESOTHO("st", "Sesotho"),
        XITSONGA("ts", "Xitsonga"),
        SWATI("ss", "siSwati"),
        VENDA("ve", "Tshivenda"),
        NDEBELE("nr", "isiNdebele");
        
        companion object {
            fun fromCode(code: String): Language {
                return values().find { it.code == code } ?: ENGLISH
            }
        }
    }
    
    fun setLocale(context: Context, language: Language): Context {
        // Handle multi-part locale codes (e.g., "nso", "nr")
        val locale = Locale(language.code)
        Locale.setDefault(locale)
        
        val config = Configuration(context.resources.configuration)
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
            // Also set default locale for the app
            Locale.setDefault(locale)
            // Create a new context with the updated configuration
            val newContext = context.createConfigurationContext(config)
            // Update the default locale for this context's resources
            val resources = newContext.resources
            val displayMetrics = resources.displayMetrics
            resources.updateConfiguration(config, displayMetrics)
            return newContext
        } else {
            @Suppress("DEPRECATION")
            config.locale = locale
            Locale.setDefault(locale)
            @Suppress("DEPRECATION")
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            return context
        }
    }
    
    /**
     * Update the app's locale configuration globally.
     * This should be called when the language changes to update the entire app.
     */
    fun updateAppLocale(context: Context, language: Language) {
        val locale = Locale(language.code)
        Locale.setDefault(locale)
        
        val config = Configuration(context.resources.configuration)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
        } else {
            @Suppress("DEPRECATION")
            config.locale = locale
            @Suppress("DEPRECATION")
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
        }
    }
    
    fun getCurrentLanguage(context: Context): Language {
        // First, try to get from saved preferences
        val savedCode = getSavedLanguageCode(context)
        if (savedCode.isNotEmpty() && savedCode != "en") {
            val language = Language.fromCode(savedCode)
            if (language != Language.ENGLISH || savedCode == "en") {
                return language
            }
        }
        
        // Fallback to system locale
        val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            @Suppress("DEPRECATION")
            context.resources.configuration.locale
        }
        // Get language code - handle both 2 and 3 character codes
        val languageCode = locale.language
        return Language.fromCode(languageCode)
    }
    
    /**
     * Get the language code from saved preferences (synchronous for context setup)
     * This is used during app initialization before coroutines can run
     */
    fun getSavedLanguageCode(context: Context): String {
        return try {
            // For synchronous access, we'll read from DataStore with runBlocking
            // This should only be called during initialization
            kotlinx.coroutines.runBlocking {
                val userPreferences = com.example.amanotes.data.local.UserPreferences(context)
                userPreferences.language.first()
            }
        } catch (e: Exception) {
            // Default to English if there's an error
            "en"
        }
    }
    
    /**
     * Force update the locale configuration for the given context.
     * This is useful when the locale needs to be updated dynamically.
     */
    fun updateConfiguration(context: Context, language: Language) {
        val locale = Locale(language.code)
        Locale.setDefault(locale)
        
        val resources = context.resources
        val config = Configuration(resources.configuration)
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
            resources.updateConfiguration(config, resources.displayMetrics)
        } else {
            @Suppress("DEPRECATION")
            config.locale = locale
            @Suppress("DEPRECATION")
            resources.updateConfiguration(config, resources.displayMetrics)
        }
    }
}

