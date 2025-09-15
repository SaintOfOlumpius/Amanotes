package com.example.amanotes.ui.compose.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onOpenProject: () -> Unit, onOpenNotes: () -> Unit, onOpenProfile: () -> Unit) {
    val todos = listOf("Find place to book", "Shared workspace", "Prepare presentation", "Review code")
    val priorities = listOf(
        "1 AM → Workout",
        "3 AM → Cash Quest presentation",
        "5 AM → Read Daily Stoic",
        "6 AM → Work on prototype",
        "7 AM → Team sync"
    )

    Scaffold(topBar = {
        TopAppBar(title = { Text("Home") }, actions = {
            TextButton(onClick = onOpenProfile) { Text("Profile") }
        })
    }) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(16.dp)) {
            item {
                Text("Hi Sthembiso", style = MaterialTheme.typography.headlineMedium)
                Text("You are almost done with today’s work.")
                Spacer(Modifier.height(16.dp))
            }

            item {
                Text("To-do list", style = MaterialTheme.typography.titleMedium)
            }
            items(todos) { t -> ListItem(headlineContent = { Text(t) }) }

            item { Spacer(Modifier.height(16.dp)) }
            item {
                Text("Daily Tasks Progress", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(progress = 0.8f, modifier = Modifier.fillMaxWidth())
                Text("8 / 10 tasks")
            }

            item { Spacer(Modifier.height(16.dp)) }
            item { Text("My Priority Tasks", style = MaterialTheme.typography.titleMedium) }
            items(priorities) { item -> ListItem(headlineContent = { Text(item) }) }

            item { Spacer(Modifier.height(24.dp)) }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Button(onClick = onOpenProject) { Text("Project Details") }
                    OutlinedButton(onClick = onOpenNotes) { Text("Notes") }
                }
            }
        }
    }
}


