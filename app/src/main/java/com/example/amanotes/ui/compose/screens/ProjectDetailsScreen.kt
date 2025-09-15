package com.example.amanotes.ui.compose.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailsScreen(onBack: () -> Unit) {
    val fields = listOf(
        "Title" to "Cash Quest",
        "In Project" to "UI Redesign",
        "Due Date" to "6 Sept 2025",
        "Status" to "In Progress",
        "Service" to "Mobile App",
        "Notes" to "Sam Love is leading this module."
    )
    val doing = listOf("PowerPoint slides", "Research gathering", "Web prototype", "App prototype")

    Scaffold(
        topBar = { TopAppBar(title = { Text("Project Details", fontWeight = FontWeight.SemiBold) }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = null) } }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Overview card
            item {
                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        fields.forEach { (label, value) ->
                            ListItem(headlineContent = { Text(label) }, supportingContent = { Text(value) })
                            Divider()
                        }
                    }
                }
            }

            // Doing list card
            item {
                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Doing", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(8.dp))
                        doing.forEach { task ->
                            ListItem(
                                leadingContent = { Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                                headlineContent = { Text(task) }
                            )
                            Divider()
                        }
                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Button(onClick = { /* mark complete */ }) { Text("Mark Complete") }
                            OutlinedButton(onClick = { /* open timeline */ }) { Text("Timeline") }
                        }
                    }
                }
            }
        }
    }
}


