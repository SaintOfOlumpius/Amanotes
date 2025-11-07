package com.example.amanotes.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
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
        val locale = Locale(language.code)
        Locale.setDefault(locale)
        
        val config = Configuration(context.resources.configuration)
        
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
            context.createConfigurationContext(config)
        } else {
            @Suppress("DEPRECATION")
            config.locale = locale
            @Suppress("DEPRECATION")
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            context
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
        val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            @Suppress("DEPRECATION")
            context.resources.configuration.locale
        }
        return Language.fromCode(locale.language)
    }
}

