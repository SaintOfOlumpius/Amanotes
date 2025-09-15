package com.example.amanotes.ui.compose.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSettingsScreen(darkMode: Boolean, onToggleDark: () -> Unit, onBack: () -> Unit) {
    val options = listOf(
        "Profile details",
        "Accessibility",
        "Passwords",
        "Notifications",
        "Dark Mode",
        "Deactivate account",
        "Settings",
        "About application",
        "Help/FAQ"
    )

    Scaffold(topBar = { TopAppBar(title = { Text("Profile & Settings") }) }) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(16.dp)) {
            item {
                Text("John Doe", style = MaterialTheme.typography.titleLarge)
                Text("Software Engineer")
                Spacer(Modifier.height(16.dp))
            }
            items(options.size) { i ->
                val label = options[i]
                if (label == "Dark Mode") {
                    ListItem(headlineContent = { Text(label) }, trailingContent = {
                        Switch(checked = darkMode, onCheckedChange = { onToggleDark() })
                    })
                } else {
                    ListItem(headlineContent = { Text(label) })
                }
                Divider()
            }
        }
    }
}


