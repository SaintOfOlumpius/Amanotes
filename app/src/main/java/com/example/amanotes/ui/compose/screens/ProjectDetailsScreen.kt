@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.amanotes.ui.compose.screens

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.amanotes.data.local.ProjectEntity
import com.example.amanotes.data.local.ProjectStatus
import com.example.amanotes.data.local.ProjectPriority
import com.example.amanotes.data.repository.ProjectRepository
import com.example.amanotes.di.ServiceLocator
import com.example.amanotes.ui.compose.components.*
import com.example.amanotes.ui.compose.theme.AmanotesColors
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val projectRepository = remember { ServiceLocator.provideProjectRepository(context) }
    val scope = rememberCoroutineScope()
    
    val allProjects by projectRepository.getAllProjects().collectAsStateWithLifecycle(initialValue = emptyList())
    var selectedStatus by remember { mutableStateOf("All") }
    var showAddDialog by remember { mutableStateOf(false) }
    var showProjectDialog by remember { mutableStateOf(false) }
    var selectedProject by remember { mutableStateOf<ProjectEntity?>(null) }
    
    // Add project dialog state
    var newProjectTitle by remember { mutableStateOf("") }
    var newProjectDescription by remember { mutableStateOf("") }
    var newProjectStatus by remember { mutableStateOf(ProjectStatus.PLANNING) }
    var newProjectPriority by remember { mutableStateOf(ProjectPriority.MEDIUM) }
    
    val snackbar = remember { SnackbarHostState() }

    // Filter projects by status
    val filteredProjects = remember(allProjects, selectedStatus) {
        if (selectedStatus == "All") {
            allProjects
        } else {
            allProjects.filter { it.status.displayName == selectedStatus }
        }
    }

    val statusOptions = listOf("All") + ProjectStatus.values().map { it.displayName }
    val activeProjects = allProjects.filter { it.status == ProjectStatus.IN_PROGRESS }
    val completedProjects = allProjects.filter { it.status == ProjectStatus.COMPLETED }

    Scaffold(
        topBar = { 
            TopAppBar(
                title = { 
                    Column {
                        Text("Projects", style = MaterialTheme.typography.titleLarge)
                        Text("${allProjects.size} total projects", style = MaterialTheme.typography.bodyMedium, color = AmanotesColors.OnSurfaceVariant)
                    }
                },
                navigationIcon = { 
                    IconButton(onClick = onBack) { 
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = AmanotesColors.Primary) 
                    } 
                },
                actions = {
                    IconButton(onClick = { /* Sort functionality */ }) {
                        Icon(Icons.Default.Sort, contentDescription = "Sort", tint = AmanotesColors.Primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AmanotesColors.Surface)
            )
        },
        floatingActionButton = {
            PremiumFloatingActionButton(
                onClick = { showAddDialog = true },
                icon = Icons.Default.Add
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbar) },
        containerColor = AmanotesColors.Background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Statistics section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricCard(
                        title = "Active",
                        value = activeProjects.size.toString(),
                        subtitle = "in progress",
                        icon = Icons.Default.PlayArrow,
                        trend = TrendDirection.Up,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Completed",
                        value = completedProjects.size.toString(),
                        subtitle = "finished",
                        icon = Icons.Default.CheckCircle,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Progress",
                        value = "${if (allProjects.isNotEmpty()) (completedProjects.size * 100 / allProjects.size) else 0}%",
                        subtitle = "overall",
                        icon = Icons.Default.TrendingUp,
                        trend = if (completedProjects.size > activeProjects.size) TrendDirection.Up else TrendDirection.Neutral,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Status filter
            item {
                PremiumCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Filter by Status",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = AmanotesColors.OnSurface
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(statusOptions) { status ->
                                FilterChip(
                                    selected = selectedStatus == status,
                                    onClick = { selectedStatus = status },
                                    label = { 
                                        Text(
                                            if (status == "All") "All (${allProjects.size})"
                                            else "$status (${allProjects.count { it.status.displayName == status }})"
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = AmanotesColors.Primary,
                                        selectedLabelColor = AmanotesColors.OnPrimary
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Projects list
            item {
                PremiumCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Your Projects",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = AmanotesColors.OnSurface
                            )
                            StatusChip(
                                text = if (filteredProjects.isEmpty()) "Empty" else "${filteredProjects.size} projects",
                                status = if (filteredProjects.isEmpty()) ChipStatus.Warning else ChipStatus.Info
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        if (filteredProjects.isEmpty()) {
                            EmptyProjectsState(
                                selectedStatus = when (selectedStatus) {
                                    "All" -> null
                                    "Planning" -> ProjectStatus.PLANNING
                                    "In Progress" -> ProjectStatus.IN_PROGRESS
                                    "On Hold" -> ProjectStatus.ON_HOLD
                                    "Completed" -> ProjectStatus.COMPLETED
                                    "Cancelled" -> ProjectStatus.CANCELLED
                                    else -> null
                                }
                            )
                        } else {
                            filteredProjects.forEach { project ->
                                ProjectCard(
                                    project = project,
                                    onClick = {
                                        selectedProject = project
                                        showProjectDialog = true
                                    }
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }
                }
            }
        }
        
        // Dialogs
        if (showAddDialog) {
            AddProjectDialog(
                title = newProjectTitle,
                description = newProjectDescription,
                status = newProjectStatus,
                priority = newProjectPriority,
                onTitleChange = { newProjectTitle = it },
                onDescriptionChange = { newProjectDescription = it },
                onStatusChange = { newProjectStatus = it },
                onPriorityChange = { newProjectPriority = it },
                onDismiss = { 
                    showAddDialog = false
                    newProjectTitle = ""
                    newProjectDescription = ""
                    newProjectStatus = ProjectStatus.PLANNING
                    newProjectPriority = ProjectPriority.MEDIUM
                },
                onConfirm = {
                    scope.launch {
                        projectRepository.insertProject(
                            newProjectTitle,
                            newProjectDescription,
                            newProjectStatus,
                            newProjectPriority,
                            null
                        )
                        showAddDialog = false
                        newProjectTitle = ""
                        newProjectDescription = ""
                        newProjectStatus = ProjectStatus.PLANNING
                        newProjectPriority = ProjectPriority.MEDIUM
                        snackbar.showSnackbar("Project created")
                    }
                }
            )
        }
        
        if (showProjectDialog && selectedProject != null) {
            ProjectDetailDialog(
                project = selectedProject!!,
                onDismiss = { 
                    showProjectDialog = false
                    selectedProject = null
                },
                onEdit = { project ->
                    scope.launch {
                        projectRepository.updateProject(project)
                        snackbar.showSnackbar("Project updated")
                    }
                },
                onDelete = { project ->
                    scope.launch {
                        projectRepository.deleteProject(project)
                        showProjectDialog = false
                        selectedProject = null
                        snackbar.showSnackbar("Project deleted")
                    }
                }
            )
        }
    }
}

// Helper Components
@Composable
private fun ProjectCard(
    project: ProjectEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    PremiumCard(
        onClick = onClick,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = project.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AmanotesColors.OnSurface,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                StatusChip(
                    text = project.status.displayName,
                    status = when (project.status) {
                        ProjectStatus.PLANNING -> ChipStatus.Info
                        ProjectStatus.IN_PROGRESS -> ChipStatus.Warning
                        ProjectStatus.ON_HOLD -> ChipStatus.Default
                        ProjectStatus.COMPLETED -> ChipStatus.Success
                        ProjectStatus.CANCELLED -> ChipStatus.Error
                    }
                )
            }
            
            if (project.description.isNotEmpty()) {
                Text(
                    text = project.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AmanotesColors.OnSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Progress",
                        style = MaterialTheme.typography.bodySmall,
                        color = AmanotesColors.OnSurfaceVariant
                    )
                    Text(
                        text = "${(project.progress * 100).toInt()}%",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = AmanotesColors.OnSurface
                    )
                }
                PremiumProgressBar(
                    progress = project.progress,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = when (project.priority) {
                            ProjectPriority.LOW -> Icons.Default.ArrowDownward
                            ProjectPriority.MEDIUM -> Icons.Default.Minimize
                            ProjectPriority.HIGH -> Icons.Default.ArrowUpward
                            ProjectPriority.URGENT -> Icons.Default.PriorityHigh
                        },
                        contentDescription = null,
                        tint = when (project.priority) {
                            ProjectPriority.LOW -> AmanotesColors.Success
                            ProjectPriority.MEDIUM -> AmanotesColors.OnSurfaceVariant
                            ProjectPriority.HIGH -> AmanotesColors.Warning
                            ProjectPriority.URGENT -> AmanotesColors.Error
                        },
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = project.priority.displayName,
                        style = MaterialTheme.typography.bodySmall,
                        color = AmanotesColors.OnSurfaceVariant
                    )
                }
                
                project.dueDate?.let { dueDate ->
                    Text(
                        text = "Due ${SimpleDateFormat("MMM dd", Locale.getDefault()).format(Date(dueDate))}",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (dueDate < System.currentTimeMillis()) AmanotesColors.Error else AmanotesColors.OnSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyProjectsState(
    selectedStatus: ProjectStatus?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Folder,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = AmanotesColors.OnSurfaceVariant.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if (selectedStatus != null) "No ${selectedStatus.displayName.lowercase()} projects" else "No projects yet",
            style = MaterialTheme.typography.titleMedium,
            color = AmanotesColors.OnSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (selectedStatus != null) "Create a project in this status" else "Tap the + button to create your first project",
            style = MaterialTheme.typography.bodyMedium,
            color = AmanotesColors.OnSurfaceVariant.copy(alpha = 0.7f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddProjectDialog(
    title: String,
    description: String,
    status: ProjectStatus,
    priority: ProjectPriority,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onStatusChange: (ProjectStatus) -> Unit,
    onPriorityChange: (ProjectPriority) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            PremiumButton(
                text = "Create Project",
                onClick = onConfirm,
                enabled = title.isNotBlank()
            )
        },
        dismissButton = {
            PremiumButton(
                text = "Cancel",
                onClick = onDismiss,
                variant = ButtonVariant.Outlined
            )
        },
        title = {
            Text(
                "Add New Project",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = AmanotesColors.OnSurface
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = onTitleChange,
                    label = { Text("Project title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AmanotesColors.Primary,
                        focusedLabelColor = AmanotesColors.Primary
                    )
                )
                
                OutlinedTextField(
                    value = description,
                    onValueChange = onDescriptionChange,
                    label = { Text("Description (optional)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    maxLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AmanotesColors.Primary,
                        focusedLabelColor = AmanotesColors.Primary
                    )
                )
                
                var showStatusDropdown by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = showStatusDropdown,
                    onExpandedChange = { showStatusDropdown = it }
                ) {
                    OutlinedTextField(
                        value = status.displayName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Status") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showStatusDropdown) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AmanotesColors.Primary,
                            focusedLabelColor = AmanotesColors.Primary
                        )
                    )
                    
                    ExposedDropdownMenu(
                        expanded = showStatusDropdown,
                        onDismissRequest = { showStatusDropdown = false }
                    ) {
                        ProjectStatus.values().forEach { statusOption ->
                            DropdownMenuItem(
                                text = { Text(statusOption.displayName) },
                                onClick = {
                                    onStatusChange(statusOption)
                                    showStatusDropdown = false
                                }
                            )
                        }
                    }
                }
                
                var showPriorityDropdown by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = showPriorityDropdown,
                    onExpandedChange = { showPriorityDropdown = it }
                ) {
                    OutlinedTextField(
                        value = priority.displayName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Priority") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showPriorityDropdown) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AmanotesColors.Primary,
                            focusedLabelColor = AmanotesColors.Primary
                        )
                    )
                    
                    ExposedDropdownMenu(
                        expanded = showPriorityDropdown,
                        onDismissRequest = { showPriorityDropdown = false }
                    ) {
                        ProjectPriority.values().forEach { priorityOption ->
                            DropdownMenuItem(
                                text = { Text(priorityOption.displayName) },
                                onClick = {
                                    onPriorityChange(priorityOption)
                                    showPriorityDropdown = false
                                }
                            )
                        }
                    }
                }
            }
        },
        containerColor = AmanotesColors.Surface,
        shape = RoundedCornerShape(16.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProjectDetailDialog(
    project: ProjectEntity,
    onDismiss: () -> Unit,
    onEdit: (ProjectEntity) -> Unit,
    onDelete: (ProjectEntity) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var editTitle by remember { mutableStateOf(project.title) }
    var editDescription by remember { mutableStateOf(project.description) }
    var editStatus by remember { mutableStateOf(project.status) }
    var editPriority by remember { mutableStateOf(project.priority) }
    var editProgress by remember { mutableStateOf(project.progress) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (isEditing) {
                    PremiumButton(
                        text = "Save",
                        onClick = {
                            onEdit(
                                project.copy(
                                    title = editTitle,
                                    description = editDescription,
                                    status = editStatus,
                                    priority = editPriority,
                                    progress = editProgress
                                )
                            )
                            isEditing = false
                        }
                    )
                    PremiumButton(
                        text = "Cancel",
                        onClick = { isEditing = false },
                        variant = ButtonVariant.Outlined
                    )
                } else {
                    PremiumButton(
                        text = "Edit",
                        onClick = { isEditing = true }
                    )
                    PremiumButton(
                        text = "Delete",
                        onClick = { onDelete(project) },
                        variant = ButtonVariant.Outlined
                    )
                }
            }
        },
        title = {
            Text(
                if (isEditing) "Edit Project" else project.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = AmanotesColors.OnSurface
            )
        },
        text = {
            if (isEditing) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = editTitle,
                        onValueChange = { editTitle = it },
                        label = { Text("Project title") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AmanotesColors.Primary,
                            focusedLabelColor = AmanotesColors.Primary
                        )
                    )
                    
                    OutlinedTextField(
                        value = editDescription,
                        onValueChange = { editDescription = it },
                        label = { Text("Description") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        maxLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AmanotesColors.Primary,
                            focusedLabelColor = AmanotesColors.Primary
                        )
                    )
                    
                    // Status and Priority dropdowns (similar to AddProjectDialog)
                    var showStatusDropdown by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = showStatusDropdown,
                        onExpandedChange = { showStatusDropdown = it }
                    ) {
                        OutlinedTextField(
                            value = editStatus.displayName,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Status") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showStatusDropdown) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AmanotesColors.Primary,
                                focusedLabelColor = AmanotesColors.Primary
                            )
                        )
                        
                        ExposedDropdownMenu(
                            expanded = showStatusDropdown,
                            onDismissRequest = { showStatusDropdown = false }
                        ) {
                            ProjectStatus.values().forEach { status ->
                                DropdownMenuItem(
                                    text = { Text(status.displayName) },
                                    onClick = {
                                        editStatus = status
                                        showStatusDropdown = false
                                    }
                                )
                            }
                        }
                    }
                    
                    // Progress slider
                    Column {
                        Text(
                            text = "Progress: ${(editProgress * 100).toInt()}%",
                            style = MaterialTheme.typography.bodyMedium,
                            color = AmanotesColors.OnSurface
                        )
                        Slider(
                            value = editProgress,
                            onValueChange = { editProgress = it },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    if (project.description.isNotEmpty()) {
                        Text(
                            text = project.description,
                            style = MaterialTheme.typography.bodyLarge,
                            color = AmanotesColors.OnSurface
                        )
                    }
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatusChip(text = project.status.displayName, status = ChipStatus.Info)
                        StatusChip(text = project.priority.displayName, status = ChipStatus.Warning)
                    }
                    
                    Column {
                        Text("Progress: ${(project.progress * 100).toInt()}%")
                        PremiumProgressBar(progress = project.progress)
                    }
                    
                    project.dueDate?.let { dueDate ->
                        Text(
                            text = "Due: ${SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(dueDate))}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = AmanotesColors.OnSurfaceVariant
                        )
                    }
                    
                    Text(
                        text = "Created ${SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(project.createdAt))}",
                        style = MaterialTheme.typography.bodySmall,
                        color = AmanotesColors.OnSurfaceVariant
                    )
                }
            }
        },
        containerColor = AmanotesColors.Surface,
        shape = RoundedCornerShape(16.dp)
    )
}


