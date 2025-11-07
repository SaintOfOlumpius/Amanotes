package com.example.amanotes.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.amanotes.R
import com.example.amanotes.utils.LocaleManager

@Composable
fun LanguageSelector(
    currentLanguage: LocaleManager.Language,
    onLanguageSelected: (LocaleManager.Language) -> Unit
) {
    val context = LocalContext.current
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        LocaleManager.Language.values().forEach { language ->
            LanguageOption(
                language = language,
                isSelected = language == currentLanguage,
                onClick = {
                    onLanguageSelected(language)
                }
            )
        }
    }
}

@Composable
fun LanguageOption(
    language: LocaleManager.Language,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val displayName = when (language) {
        LocaleManager.Language.ENGLISH -> context.getString(R.string.language_english)
        LocaleManager.Language.AFRIKAANS -> context.getString(R.string.language_afrikaans)
        LocaleManager.Language.ZULU -> context.getString(R.string.language_zulu)
        LocaleManager.Language.XHOSA -> context.getString(R.string.language_xhosa)
        LocaleManager.Language.SEPEDI -> context.getString(R.string.language_sepedi)
        LocaleManager.Language.SETSWANA -> context.getString(R.string.language_setswana)
        LocaleManager.Language.SESOTHO -> context.getString(R.string.language_sesotho)
        LocaleManager.Language.XITSONGA -> context.getString(R.string.language_xitsonga)
        LocaleManager.Language.SWATI -> context.getString(R.string.language_swati)
        LocaleManager.Language.VENDA -> context.getString(R.string.language_venda)
        LocaleManager.Language.NDEBELE -> context.getString(R.string.language_ndebele)
    }
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = displayName,
                style = MaterialTheme.typography.bodyLarge
            )
            if (isSelected) {
                RadioButton(
                    selected = true,
                    onClick = null
                )
            }
        }
    }
}

