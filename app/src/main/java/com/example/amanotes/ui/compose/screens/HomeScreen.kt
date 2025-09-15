package com.example.amanotes.ui.compose.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onOpenProject: () -> Unit, onOpenNotes: () -> Unit, onOpenProfile: () -> Unit) {
    data class Task(val title: String, val done: Boolean = false)
    val tasks = remember {
        mutableStateListOf(
            Task("Find place to book"),
            Task("Shared workspace"),
            Task("Prepare presentation"),
            Task("Review code")
        )
    }
    var newTaskTitle by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }
    var filter by remember { mutableStateOf("All") }
    val snackbar = remember { SnackbarHostState() }

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
            FloatingActionButton(onClick = { showAddDialog = true }) { Icon(Icons.Default.Add, contentDescription = "Add Task") }
        },
        snackbarHost = { SnackbarHost(hostState = snackbar) }
    ) { padding ->
        val visibleTasks = when (filter) {
            "Pending" -> tasks.filter { !it.done }
            "Done" -> tasks.filter { it.done }
            else -> tasks
        }
        val completedCount = tasks.count { it.done }
        val progress = if (tasks.isEmpty()) 0f else completedCount.toFloat() / tasks.size.toFloat()

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
                        LinearProgressIndicator(progress = progress.coerceIn(0f, 1f), modifier = Modifier.fillMaxWidth())
                        Spacer(Modifier.height(6.dp))
                        Text("$completedCount of ${tasks.size} tasks", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            // Filters
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(selected = filter == "All", onClick = { filter = "All" }, label = { Text("All") })
                    FilterChip(selected = filter == "Pending", onClick = { filter = "Pending" }, label = { Text("Pending") })
                    FilterChip(selected = filter == "Done", onClick = { filter = "Done" }, label = { Text("Done") })
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
                        visibleTasks.forEach { task ->
                            ListItem(
                                headlineContent = { Text(task.title) },
                                supportingContent = { Text(if (task.done) "Completed" else "Due today", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                                trailingContent = {
                                    Checkbox(checked = task.done, onCheckedChange = { checked ->
                                        val index = tasks.indexOf(task)
                                        if (index >= 0) tasks[index] = tasks[index].copy(done = checked)
                                    })
                                }
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
                        val priorities = listOf(
                            "1 AM → Workout",
                            "3 AM → Cash Quest presentation",
                            "5 AM → Read Daily Stoic",
                            "6 AM → Work on prototype",
                            "7 AM → Team sync"
                        )
                        priorities.forEachIndexed { index, item ->
                            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
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

        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        val title = newTaskTitle.trim()
                        if (title.isNotEmpty()) {
                            tasks.add(Task(title))
                            newTaskTitle = ""
                        }
                        showAddDialog = false
                    }) { Text("Add") }
                },
                dismissButton = { TextButton(onClick = { showAddDialog = false }) { Text("Cancel") } },
                title = { Text("Add Task") },
                text = {
                    OutlinedTextField(
                        value = newTaskTitle,
                        onValueChange = { newTaskTitle = it },
                        label = { Text("Task title") },
                        singleLine = true
                    )
                }
            )
        }
    }
}


