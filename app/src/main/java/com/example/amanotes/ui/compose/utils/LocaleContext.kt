package com.example.amanotes.ui.compose.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.amanotes.utils.LocaleManager
import java.util.Locale

/**
 * Provides a locale-aware context that updates reactively when language changes.
 * When languageCode changes, the context is recreated and all child composables
 * will recompose with the new locale.
 * 
 * This uses CompositionLocalProvider to provide a locale-aware context to all
 * child composables, so stringResource() will automatically use the correct locale.
 */
@Composable
fun LocaleAwareContent(
    languageCode: String,
    content: @Composable () -> Unit
) {
    // Get the base context (activity context)
    val baseContext = LocalContext.current
    
    // Recreate context when language changes - this will trigger recomposition
    // The context is created with the locale from languageCode
    val localeContext = remember(languageCode, baseContext) {
        val language = LocaleManager.Language.fromCode(languageCode)
        val locale = Locale(language.code)
        Locale.setDefault(locale)
        
        val config = Configuration(baseContext.resources.configuration)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
            // Create a new context with the updated locale
            // This context will have resources in the correct language
            baseContext.createConfigurationContext(config)
        } else {
            @Suppress("DEPRECATION")
            config.locale = locale
            @Suppress("DEPRECATION")
            baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
            baseContext
        }
    }
    
    // Update global locale setting when language changes
    LaunchedEffect(languageCode) {
        val language = LocaleManager.Language.fromCode(languageCode)
        val locale = Locale(language.code)
        Locale.setDefault(locale)
    }
    
    // Provide the locale-aware context to all children via CompositionLocalProvider
    // This ensures that LocalContext.current in child composables returns the locale-aware context
    CompositionLocalProvider(
        androidx.compose.ui.platform.LocalContext provides localeContext
    ) {
        // Key the content by language code to force complete recomposition when language changes
        // This ensures all stringResource() calls use the new locale
        key(languageCode) {
            content()
        }
    }
}
