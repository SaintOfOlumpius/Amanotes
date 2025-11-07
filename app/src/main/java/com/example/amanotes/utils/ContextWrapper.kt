package com.example.amanotes.utils

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

class LocaleContextWrapper(base: Context) : ContextWrapper(base) {
    companion object {
        fun wrap(context: Context, language: LocaleManager.Language): ContextWrapper {
            val config = context.resources.configuration
            val locale = Locale(language.code)
            Locale.setDefault(locale)
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                config.setLocale(locale)
                return LocaleContextWrapper(context.createConfigurationContext(config))
            } else {
                @Suppress("DEPRECATION")
                config.locale = locale
                @Suppress("DEPRECATION")
                context.resources.updateConfiguration(config, context.resources.displayMetrics)
                return LocaleContextWrapper(context)
            }
        }
    }
}

