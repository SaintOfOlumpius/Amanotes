package com.example.amanotes.ui.compose.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.amanotes.data.local.TaskEntity
import com.example.amanotes.data.repository.TaskRepository
import com.example.amanotes.di.ServiceLocator
import com.example.amanotes.ui.compose.components.*
import com.example.amanotes.ui.compose.theme.AmanotesColors
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onOpenProject: () -> Unit, onOpenNotes: () -> Unit, onOpenProfile: () -> Unit) {
    val context = LocalContext.current
    val taskRepository = remember { ServiceLocator.provideTaskRepository(context) }
    val scope = rememberCoroutineScope()
    
    val allTasks by taskRepository.getAllTasks().collectAsStateWithLifecycle(initialValue = emptyList())
    var newTaskTitle by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }
    var filter by remember { mutableStateOf("All") }
    val snackbar = remember { SnackbarHostState() }

    // Calculate statistics
    val completedCount = allTasks.count { it.isCompleted }
    val progress = if (allTasks.isEmpty()) 0f else completedCount.toFloat() / allTasks.size.toFloat()
    val todayTasks = allTasks.filter { !it.isCompleted }
    
    // Get current time info
    val currentDate = remember { SimpleDateFormat("EEEE, MMM dd", Locale.getDefault()).format(Date()) }
    val currentTime = remember { SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()) }

    // Quick actions
    val quickActions = listOf(
        QuickAction("New Note", Icons.Default.Create, onOpenNotes),
        QuickAction("Projects", Icons.Default.Folder, onOpenProject),
        QuickAction("Settings", Icons.Default.Settings, onOpenProfile),
        QuickAction("Analytics", Icons.Default.Analytics) { /* TODO */ }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("Welcome back!", style = MaterialTheme.typography.titleLarge)
                        Text(currentDate, style = MaterialTheme.typography.bodyMedium, color = AmanotesColors.OnSurfaceVariant)
                    }
                },
                actions = {
                    IconButton(onClick = onOpenProfile) { 
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = AmanotesColors.Primary) 
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AmanotesColors.Surface)
            )
        },
        floatingActionButton = {
            PremiumFloatingActionButton(onClick = { showAddDialog = true })
        },
        snackbarHost = { SnackbarHost(hostState = snackbar) },
        containerColor = AmanotesColors.Background
    ) { padding ->
        val visibleTasks = when (filter) {
            "Pending" -> allTasks.filter { !it.isCompleted }
            "Done" -> allTasks.filter { it.isCompleted }
            else -> allTasks
        }

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Hero section with gradient background
            item {
                GradientCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Good ${getGreeting()}!",
                                style = MaterialTheme.typography.headlineMedium,
                                color = AmanotesColors.OnPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (todayTasks.isEmpty()) "All tasks completed! 🎉" 
                                      else "${todayTasks.size} tasks remaining",
                                style = MaterialTheme.typography.bodyLarge,
                                color = AmanotesColors.OnPrimary.copy(alpha = 0.9f)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            PremiumProgressBar(
                                progress = progress,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.Dashboard,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = AmanotesColors.OnPrimary.copy(alpha = 0.3f)
                        )
                    }
                }
            }

            // Statistics cards
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricCard(
                        title = "Completed",
                        value = completedCount.toString(),
                        subtitle = "tasks today",
                        icon = Icons.Default.CheckCircle,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Pending",
                        value = todayTasks.size.toString(),
                        subtitle = "remaining",
                        icon = Icons.Default.Schedule,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Progress",
                        value = "${(progress * 100).toInt()}%",
                        subtitle = "completion",
                        icon = Icons.Default.TrendingUp,
                        trend = if (progress > 0.5f) TrendDirection.Up else TrendDirection.Neutral,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Quick actions
            item {
                PremiumCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Quick Actions",
                            style = MaterialTheme.typography.titleMedium,
                            color = AmanotesColors.OnSurface,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(quickActions) { action ->
                                QuickActionButton(action)
                            }
                        }
                    }
                }
            }

            // Filter section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = filter == "All",
                        onClick = { filter = "All" },
                        label = { Text("All (${allTasks.size})") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AmanotesColors.Primary,
                            selectedLabelColor = AmanotesColors.OnPrimary
                        )
                    )
                    FilterChip(
                        selected = filter == "Pending",
                        onClick = { filter = "Pending" },
                        label = { Text("Pending (${todayTasks.size})") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AmanotesColors.Warning,
                            selectedLabelColor = AmanotesColors.OnPrimary
                        )
                    )
                    FilterChip(
                        selected = filter == "Done",
                        onClick = { filter = "Done" },
                        label = { Text("Done ($completedCount)") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AmanotesColors.Success,
                            selectedLabelColor = AmanotesColors.OnPrimary
                        )
                    )
                }
            }

            // Tasks section
            item {
                PremiumCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.TaskAlt,
                                    contentDescription = null,
                                    tint = AmanotesColors.Primary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Today's Tasks",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AmanotesColors.OnSurface
                                )
                            }
                            StatusChip(
                                text = when (filter) {
                                    "Pending" -> "Pending"
                                    "Done" -> "Completed"
                                    else -> "All Tasks"
                                },
                                status = when (filter) {
                                    "Pending" -> ChipStatus.Warning
                                    "Done" -> ChipStatus.Success
                                    else -> ChipStatus.Info
                                }
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        if (visibleTasks.isEmpty()) {
                            EmptyTasksState(filter = filter)
                        } else {
                            visibleTasks.forEach { task ->
                                SwipeableTaskItem(
                                    task = task,
                                    onToggleComplete = { taskToToggle ->
                                        scope.launch {
                                            taskRepository.toggleTaskCompletion(taskToToggle)
                                        }
                                    },
                                    onDelete = { taskToDelete ->
                                        scope.launch {
                                            taskRepository.deleteTask(taskToDelete)
                                            snackbar.showSnackbar("Task deleted")
                                        }
                                    }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }

        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                confirmButton = {
                    PremiumButton(
                        text = "Add Task",
                        onClick = {
                            val title = newTaskTitle.trim()
                            if (title.isNotEmpty()) {
                                scope.launch {
                                    taskRepository.insertTask(title)
                                    newTaskTitle = ""
                                    showAddDialog = false
                                }
                            } else {
                                showAddDialog = false
                            }
                        }
                    )
                },
                dismissButton = { 
                    PremiumButton(
                        text = "Cancel",
                        onClick = { showAddDialog = false },
                        variant = ButtonVariant.Outlined
                    )
                },
                title = { 
                    Text(
                        "Add New Task",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = AmanotesColors.OnSurface
                    )
                },
                text = {
                    OutlinedTextField(
                        value = newTaskTitle,
                        onValueChange = { newTaskTitle = it },
                        label = { Text("Task title") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AmanotesColors.Primary,
                            focusedLabelColor = AmanotesColors.Primary
                        )
                    )
                },
                containerColor = AmanotesColors.Surface,
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}

// Helper functions and data classes
private fun getGreeting(): String {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when (hour) {
        in 0..11 -> "morning"
        in 12..17 -> "afternoon"
        else -> "evening"
    }
}

data class QuickAction(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val onClick: () -> Unit
)

@Composable
private fun QuickActionButton(action: QuickAction) {
    PremiumCard(
        onClick = action.onClick,
        modifier = Modifier.width(120.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = action.icon,
                contentDescription = null,
                tint = AmanotesColors.Primary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = action.title,
                style = MaterialTheme.typography.bodyMedium,
                color = AmanotesColors.OnSurface,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun EmptyTasksState(filter: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = when (filter) {
                "Done" -> Icons.Default.CheckCircle
                "Pending" -> Icons.Default.Schedule
                else -> Icons.Default.TaskAlt
            },
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = AmanotesColors.OnSurfaceVariant.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = when (filter) {
                "Done" -> "No completed tasks yet"
                "Pending" -> "No pending tasks"
                else -> "No tasks yet"
            },
            style = MaterialTheme.typography.titleMedium,
            color = AmanotesColors.OnSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = when (filter) {
                "Done" -> "Complete some tasks to see them here"
                "Pending" -> "Great! All tasks are completed"
                else -> "Tap the + button to create your first task"
            },
            style = MaterialTheme.typography.bodyMedium,
            color = AmanotesColors.OnSurfaceVariant.copy(alpha = 0.7f)
        )
    }
}


