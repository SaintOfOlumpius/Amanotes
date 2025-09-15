package com.example.amanotes.ui.compose.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

    Scaffold(topBar = { TopAppBar(title = { Text("Project Details") }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = null) } }) }) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(16.dp)) {
            items(fields.size) { i ->
                val (label, value) = fields[i]
                ListItem(headlineContent = { Text(label) }, supportingContent = { Text(value) })
                Divider()
            }

            item { Spacer(Modifier.height(16.dp)) }
            item { Text("Doing", style = MaterialTheme.typography.titleMedium) }
            items(doing.size) { i -> ListItem(headlineContent = { Text(doing[i]) }) }
        }
    }
}


