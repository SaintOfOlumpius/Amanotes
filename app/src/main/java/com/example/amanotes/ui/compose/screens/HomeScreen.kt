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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.amanotes.R
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.amanotes.data.local.TaskEntity
import com.example.amanotes.data.repository.TaskRepository
import com.example.amanotes.data.repository.NoteRepository
import com.example.amanotes.data.repository.ProjectRepository
import com.example.amanotes.data.repository.AuthRepository
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
    val noteRepository = remember { ServiceLocator.provideNoteRepository(context) }
    val projectRepository = remember { ServiceLocator.provideProjectRepository(context) }
    val authRepository = remember { ServiceLocator.provideAuthRepository(context) }
    val scope = rememberCoroutineScope()
    
    // Data states
    val allTasks by taskRepository.getAllTasks().collectAsStateWithLifecycle(initialValue = emptyList())
    val allNotes by noteRepository.getAllNotes().collectAsStateWithLifecycle(initialValue = emptyList())
    val allProjects by projectRepository.getAllProjects().collectAsStateWithLifecycle(initialValue = emptyList())
    
    // UI states
    var newTaskTitle by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }
    var filter by remember { mutableStateOf("All") }
    val snackbar = remember { SnackbarHostState() }
    
    // User state
    var currentUser by remember { mutableStateOf<com.example.amanotes.data.local.UserEntity?>(null) }
    
    // Load current user
    LaunchedEffect(Unit) {
        currentUser = authRepository.getCurrentUser()
    }

    // Calculate comprehensive statistics
    val completedCount = allTasks.count { it.isCompleted }
    val progress = if (allTasks.isEmpty()) 0f else completedCount.toFloat() / allTasks.size.toFloat()
    val todayTasks = allTasks.filter { !it.isCompleted }
    val recentNotes = allNotes.take(3)
    val activeProjects = allProjects.filter { it.status == com.example.amanotes.data.local.ProjectStatus.IN_PROGRESS }
    val completedProjects = allProjects.count { it.status == com.example.amanotes.data.local.ProjectStatus.COMPLETED }
    
    // Get current time info - use context to get proper locale
    val currentLocale = remember { 
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            @Suppress("DEPRECATION")
            context.resources.configuration.locale
        }
    }
    val currentDate = remember(currentLocale) { 
        SimpleDateFormat("EEEE, MMM dd", currentLocale).format(Date()) 
    }
    val currentTime = remember(currentLocale) { 
        SimpleDateFormat("HH:mm", currentLocale).format(Date()) 
    }

    // Quick actions
    val quickActions = remember {
        listOf(
            QuickAction(context.getString(R.string.new_note), Icons.Default.Create, onOpenNotes),
            QuickAction(context.getString(R.string.projects), Icons.Default.Folder, onOpenProject),
            QuickAction(context.getString(R.string.settings), Icons.Default.Settings, onOpenProfile),
            QuickAction(context.getString(R.string.analytics), Icons.Default.Analytics) { /* TODO */ }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text(
                            text = currentUser?.name?.let { name ->
                                stringResource(R.string.welcome_back, name.split(" ").firstOrNull() ?: "Scholar")
                            } ?: stringResource(R.string.welcome_scholar), 
                            style = MaterialTheme.typography.titleLarge,
                            color = AmanotesColors.OnSurface
                        )
                        Text(
                            text = currentDate, 
                            style = MaterialTheme.typography.bodyMedium, 
                            color = AmanotesColors.OnSurfaceVariant
                        )
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
                                text = getGreeting(),
                                style = MaterialTheme.typography.headlineMedium,
                                color = AmanotesColors.OnPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (todayTasks.isEmpty()) {
                                    stringResource(R.string.all_tasks_completed)
                                } else {
                                    stringResource(R.string.tasks_remaining, todayTasks.size)
                                },
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
                        title = stringResource(R.string.completed),
                        value = completedCount.toString(),
                        subtitle = stringResource(R.string.tasks_today),
                        icon = Icons.Default.CheckCircle,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = stringResource(R.string.pending),
                        value = todayTasks.size.toString(),
                        subtitle = stringResource(R.string.remaining),
                        icon = Icons.Default.Schedule,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = stringResource(R.string.progress),
                        value = "${(progress * 100).toInt()}%",
                        subtitle = stringResource(R.string.completion),
                        icon = Icons.Default.TrendingUp,
                        trend = if (progress > 0.5f) TrendDirection.Up else TrendDirection.Neutral,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Recent Notes Preview
            if (recentNotes.isNotEmpty()) {
                item {
                    PremiumCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(R.string.recent_notes),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AmanotesColors.OnSurface
                                )
                                PremiumButton(
                                    text = stringResource(R.string.view_all),
                                    onClick = onOpenNotes,
                                    variant = ButtonVariant.Outlined
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            recentNotes.forEach { note ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Description,
                                        contentDescription = null,
                                        tint = AmanotesColors.Primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = note.title,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Medium,
                                            color = AmanotesColors.OnSurface,
                                            maxLines = 1
                                        )
                                        Text(
                                            text = note.category,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = AmanotesColors.OnSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            // Active Projects Preview
            if (activeProjects.isNotEmpty()) {
                item {
                    PremiumCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(R.string.active_projects),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AmanotesColors.OnSurface
                                )
                                PremiumButton(
                                    text = stringResource(R.string.view_all),
                                    onClick = onOpenProject,
                                    variant = ButtonVariant.Outlined
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            activeProjects.take(3).forEach { project ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Folder,
                                        contentDescription = null,
                                        tint = AmanotesColors.Secondary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = project.title,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Medium,
                                            color = AmanotesColors.OnSurface,
                                            maxLines = 1
                                        )
                                        Text(
                                            text = stringResource(R.string.percent_complete, (project.progress * 100).toInt()),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = AmanotesColors.OnSurfaceVariant
                                        )
                                    }
                                    LinearProgressIndicator(
                                        progress = { project.progress },
                                        modifier = Modifier
                                            .width(60.dp)
                                            .height(4.dp),
                                        color = AmanotesColors.Primary,
                                        trackColor = AmanotesColors.SurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Quick actions
            item {
                PremiumCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = stringResource(R.string.quick_actions),
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
                        label = { Text("${stringResource(R.string.all_tasks)} (${allTasks.size})") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AmanotesColors.Primary,
                            selectedLabelColor = AmanotesColors.OnPrimary
                        )
                    )
                    FilterChip(
                        selected = filter == "Pending",
                        onClick = { filter = "Pending" },
                        label = { Text("${stringResource(R.string.pending)} (${todayTasks.size})") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AmanotesColors.Warning,
                            selectedLabelColor = AmanotesColors.OnPrimary
                        )
                    )
                    FilterChip(
                        selected = filter == "Done",
                        onClick = { filter = "Done" },
                        label = { Text("${stringResource(R.string.completed)} ($completedCount)") },
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
                                    text = stringResource(R.string.todays_tasks),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AmanotesColors.OnSurface
                                )
                            }
                            StatusChip(
                                text = when (filter) {
                                    "Pending" -> stringResource(R.string.pending)
                                    "Done" -> stringResource(R.string.completed)
                                    else -> stringResource(R.string.all_tasks)
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
                        text = stringResource(R.string.add_task),
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
                        text = stringResource(R.string.cancel),
                        onClick = { showAddDialog = false },
                        variant = ButtonVariant.Outlined
                    )
                },
                title = { 
                    Text(
                        stringResource(R.string.add_new_task),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = AmanotesColors.OnSurface
                    )
                },
                text = {
                    OutlinedTextField(
                        value = newTaskTitle,
                        onValueChange = { newTaskTitle = it },
                        label = { Text(stringResource(R.string.task_title)) },
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
@Composable
private fun getGreeting(): String {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when (hour) {
        in 0..11 -> stringResource(R.string.good_morning)
        in 12..17 -> stringResource(R.string.good_afternoon)
        else -> stringResource(R.string.good_evening)
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
                "Done" -> stringResource(R.string.no_completed_tasks)
                "Pending" -> stringResource(R.string.no_pending_tasks)
                else -> stringResource(R.string.no_tasks_yet)
            },
            style = MaterialTheme.typography.titleMedium,
            color = AmanotesColors.OnSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = when (filter) {
                "Done" -> stringResource(R.string.complete_some_tasks)
                "Pending" -> stringResource(R.string.all_tasks_completed_msg)
                else -> stringResource(R.string.create_first_task)
            },
            style = MaterialTheme.typography.bodyMedium,
            color = AmanotesColors.OnSurfaceVariant.copy(alpha = 0.7f)
        )
    }
}


