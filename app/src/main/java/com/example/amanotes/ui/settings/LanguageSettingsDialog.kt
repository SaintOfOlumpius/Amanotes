package com.example.amanotes.ui.settings

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.amanotes.MainActivity
import com.example.amanotes.R
import com.example.amanotes.ui.components.LanguageSelector
import com.example.amanotes.utils.LocaleManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun LanguageSettingsDialog(
    onDismiss: () -> Unit,
    viewModel: SettingsViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    var currentLanguage by remember { mutableStateOf(LocaleManager.Language.ENGLISH) }
    
    LaunchedEffect(Unit) {
        val languageCode = viewModel.language.first()
        currentLanguage = LocaleManager.Language.fromCode(languageCode)
    }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.select_language))
        },
        text = {
            LanguageSelector(
                currentLanguage = currentLanguage,
                onLanguageSelected = { language ->
                    if (language != currentLanguage) {
                        currentLanguage = language
                        scope.launch {
                            try {
                                // Save the language preference and wait for it to complete
                                viewModel.updateLanguageAndWait(language.code)
                                
                                // Verify it was saved by checking the flow
                                val savedLanguage = viewModel.language.first()
                                if (savedLanguage == language.code) {
                                    // Close dialog first
                                    onDismiss()
                                    // Small delay to let dialog close animation complete
                                    kotlinx.coroutines.delay(150)
                                    // Then restart the activity to apply the new locale
                                    if (context is Activity) {
                                        restartActivity(context)
                                    }
                                } else {
                                    // If save didn't work, just close dialog
                                    onDismiss()
                                }
                            } catch (e: Exception) {
                                // If there's an error, just close the dialog
                                onDismiss()
                            }
                        }
                    }
                }
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.close))
            }
        }
    )
}

/**
 * Restart the activity to apply the new language
 * Using recreate() is more reliable than killing the process
 */
private fun restartActivity(activity: Activity) {
    // Use recreate() which properly recreates the activity with new configuration
    activity.recreate()
}

