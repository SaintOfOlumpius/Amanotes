package com.example.amanotes.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
                                // Save the language preference - UI will update automatically
                                viewModel.updateLanguageAndWait(language.code)
                                // Close dialog - no restart needed!
                                onDismiss()
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


