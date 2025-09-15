package com.example.amanotes.ui.compose.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Amanotes", fontWeight = FontWeight.SemiBold) },
                actions = {
                    IconButton(onClick = onOpenProfile) { Icon(Icons.Default.Notifications, contentDescription = null) }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onOpenNotes) { Icon(Icons.Default.Add, contentDescription = "New Note") }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Greeting + summary
            item {
                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(20.dp)) {
                        Text("Hi Sthembiso", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(4.dp))
                        Text("You are almost done with today’s work.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(16.dp))
                        Text("Daily Progress", style = MaterialTheme.typography.titleSmall)
                        Spacer(Modifier.height(6.dp))
                        LinearProgressIndicator(progress = 0.8f, modifier = Modifier.fillMaxWidth())
                        Spacer(Modifier.height(6.dp))
                        Text("8 of 10 tasks", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            // Quick actions chips
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AssistChip(onClick = onOpenNotes, label = { Text("All Notes") })
                    AssistChip(onClick = onOpenProject, label = { Text("Projects") })
                    AssistChip(onClick = onOpenProfile, label = { Text("Profile") })
                }
            }

            // To‑do list card
            item {
                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.TaskAlt, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Text("Today’s To‑dos", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(Modifier.height(8.dp))
                        todos.forEach { t ->
                            ListItem(
                                headlineContent = { Text(t) },
                                supportingContent = { Text("Due today", color = MaterialTheme.colorScheme.onSurfaceVariant) }
                            )
                            Divider()
                        }
                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Button(onClick = onOpenNotes) { Text("View Notes") }
                            OutlinedButton(onClick = onOpenProject) { Text("Open Project") }
                        }
                    }
                }
            }

            // Priority timeline
            item {
                ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text("My Priority Timeline", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(8.dp))
                        priorities.forEachIndexed { index, item ->
                            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                                // timeline indicator
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(Modifier.size(10.dp)) {
                                        Surface(color = MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.small) { Box(Modifier.size(10.dp)) }
                                    }
                                    if (index != priorities.lastIndex) {
                                        Divider(
                                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                                            modifier = Modifier
                                                .width(1.dp)
                                                .height(28.dp)
                                        )
                                    }
                                }
                                Spacer(Modifier.width(12.dp))
                                Text(item, style = MaterialTheme.typography.bodyLarge)
                            }
                            if (index != priorities.lastIndex) Spacer(Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}


