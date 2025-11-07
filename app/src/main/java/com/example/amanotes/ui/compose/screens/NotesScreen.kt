@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.amanotes.ui.compose.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
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
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.amanotes.R
import com.example.amanotes.data.local.NoteEntity
import com.example.amanotes.data.repository.NoteRepository
import com.example.amanotes.di.ServiceLocator
import com.example.amanotes.ui.compose.components.*
import com.example.amanotes.ui.compose.theme.AmanotesColors
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val noteRepository = remember { ServiceLocator.provideNoteRepository(context) }
    val scope = rememberCoroutineScope()
    
    val allNotes by noteRepository.getAllNotes().collectAsStateWithLifecycle(initialValue = emptyList())
    val allCategories by noteRepository.getAllCategories().collectAsStateWithLifecycle(initialValue = emptyList())
    
    var selectedCategory by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }
    var showNoteDialog by remember { mutableStateOf(false) }
    var selectedNote by remember { mutableStateOf<NoteEntity?>(null) }
    var isGridView by remember { mutableStateOf(true) }
    val snackbar = remember { SnackbarHostState() }

    // Filter notes based on category and search
    val filteredNotes = remember(allNotes, selectedCategory, searchQuery) {
        allNotes.filter { note ->
            val matchesCategory = selectedCategory == "All" || note.category == selectedCategory
            val matchesSearch = searchQuery.isEmpty() || 
                note.title.contains(searchQuery, ignoreCase = true) ||
                note.content.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    val categories = listOf("All") + allCategories

    Scaffold(
        topBar = { 
            TopAppBar(
                title = { 
                    Column {
                        Text(stringResource(R.string.notes_title), style = MaterialTheme.typography.titleLarge)
                        Text(stringResource(R.string.notes_count, allNotes.size), style = MaterialTheme.typography.bodyMedium, color = AmanotesColors.OnSurfaceVariant)
                    }
                },
                actions = {
                    IconButton(onClick = { isGridView = !isGridView }) {
                        Icon(
                            imageVector = if (isGridView) Icons.Default.ViewList else Icons.Default.GridView,
                            contentDescription = stringResource(R.string.toggle_view_description),
                            tint = AmanotesColors.Primary
                        )
                    }
                    IconButton(onClick = { /* Search functionality */ }) {
                        Icon(Icons.Default.Search, contentDescription = stringResource(R.string.search_description), tint = AmanotesColors.Primary)
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
            // Search bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(stringResource(R.string.search_notes)) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = AmanotesColors.Primary) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear search")
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AmanotesColors.Primary,
                        focusedLabelColor = AmanotesColors.Primary
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            }

            // Category filter chips
            item {
                PremiumCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Categories",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = AmanotesColors.OnSurface
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(categories) { category ->
                                FilterChip(
                                    selected = selectedCategory == category,
                                    onClick = { selectedCategory = category },
                                    label = { 
                                        Text(
                                            if (category == "All") "All (${allNotes.size})" 
                                            else "$category (${allNotes.count { it.category == category }})"
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

            // Statistics
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricCard(
                        title = "Total Notes",
                        value = allNotes.size.toString(),
                        subtitle = "created",
                        icon = Icons.Default.Description,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Categories",
                        value = allCategories.size.toString(),
                        subtitle = "organized",
                        icon = Icons.Default.Category,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Favorites",
                        value = allNotes.count { it.isFavorite }.toString(),
                        subtitle = "starred",
                        icon = Icons.Default.Star,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Notes content
            item {
                PremiumCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Your Notes",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = AmanotesColors.OnSurface
                            )
                            StatusChip(
                                text = if (filteredNotes.isEmpty()) "Empty" else "${filteredNotes.size} notes",
                                status = if (filteredNotes.isEmpty()) ChipStatus.Warning else ChipStatus.Info
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        if (filteredNotes.isEmpty()) {
                            EmptyNotesState(selectedCategory = selectedCategory, hasSearch = searchQuery.isNotEmpty())
                        } else {
                            if (isGridView) {
                                LazyVerticalStaggeredGrid(
                                    columns = StaggeredGridCells.Fixed(2),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalItemSpacing = 12.dp,
                                    modifier = Modifier.height(400.dp)
                                ) {
                                    items(filteredNotes) { note ->
                                        NoteCard(
                                            note = note,
                                            onClick = { 
                                                selectedNote = note
                                                showNoteDialog = true
                                            },
                                            onFavoriteClick = {
                                                scope.launch {
                                                    noteRepository.toggleFavorite(note)
                                                }
                                            }
                                        )
                                    }
                                }
                            } else {
                                filteredNotes.forEach { note ->
                                    NoteListItem(
                                        note = note,
                                        onClick = {
                                            selectedNote = note
                                            showNoteDialog = true
                                        },
                                        onFavoriteClick = {
                                            scope.launch {
                                                noteRepository.toggleFavorite(note)
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
        }
        
        // Dialogs
        if (showAddDialog) {
            AddNoteDialog(
                onDismiss = { showAddDialog = false },
                onSave = { title, content, category ->
                    scope.launch {
                        noteRepository.insertNote(title, content, category)
                        showAddDialog = false
                        snackbar.showSnackbar("Note created")
                    }
                },
                categories = allCategories
            )
        }
        
        if (showNoteDialog && selectedNote != null) {
            NoteDetailDialog(
                note = selectedNote!!,
                onDismiss = { 
                    showNoteDialog = false
                    selectedNote = null
                },
                onEdit = { note ->
                    scope.launch {
                        noteRepository.updateNote(note)
                        snackbar.showSnackbar("Note updated")
                    }
                },
                onDelete = { note ->
                    scope.launch {
                        noteRepository.deleteNote(note)
                        showNoteDialog = false
                        selectedNote = null
                        snackbar.showSnackbar("Note deleted")
                    }
                },
                categories = allCategories
            )
        }
    }
}

// Helper composables
@Composable
private fun NoteCard(
    note: NoteEntity,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    PremiumCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AmanotesColors.OnSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = if (note.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = "Favorite",
                        tint = if (note.isFavorite) AmanotesColors.Warning else AmanotesColors.OnSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium,
                color = AmanotesColors.OnSurfaceVariant,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusChip(
                    text = note.category,
                    status = ChipStatus.Default
                )
                Text(
                    text = SimpleDateFormat("MMM dd", Locale.getDefault()).format(Date(note.updatedAt)),
                    style = MaterialTheme.typography.bodySmall,
                    color = AmanotesColors.OnSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun NoteListItem(
    note: NoteEntity,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    PremiumCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        ListItem(
            headlineContent = { 
                Text(
                    text = note.title,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            supportingContent = { 
                Text(
                    text = note.content,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = AmanotesColors.OnSurfaceVariant
                )
            },
            trailingContent = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    StatusChip(text = note.category, status = ChipStatus.Default)
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = onFavoriteClick) {
                        Icon(
                            imageVector = if (note.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = "Favorite",
                            tint = if (note.isFavorite) AmanotesColors.Warning else AmanotesColors.OnSurfaceVariant
                        )
                    }
                }
            }
        )
    }
}

@Composable
private fun EmptyNotesState(selectedCategory: String, hasSearch: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = if (hasSearch) Icons.Default.SearchOff else Icons.Default.Description,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = AmanotesColors.OnSurfaceVariant.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = when {
                hasSearch -> "No notes found"
                selectedCategory == "All" -> "No notes yet"
                else -> "No notes in $selectedCategory"
            },
            style = MaterialTheme.typography.titleMedium,
            color = AmanotesColors.OnSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = when {
                hasSearch -> "Try adjusting your search terms"
                selectedCategory == "All" -> "Tap the + button to create your first note"
                else -> "Create a note in this category to see it here"
            },
            style = MaterialTheme.typography.bodyMedium,
            color = AmanotesColors.OnSurfaceVariant.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun AddNoteDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit,
    categories: List<String>
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("General") }
    var showCategoryDropdown by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            PremiumButton(
                text = "Save Note",
                onClick = {
                    if (title.isNotBlank() && content.isNotBlank()) {
                        onSave(title.trim(), content.trim(), selectedCategory)
                    }
                }
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
                "Create New Note",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(stringResource(R.string.note_title)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AmanotesColors.Primary,
                        focusedLabelColor = AmanotesColors.Primary
                    )
                )
                
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text(stringResource(R.string.note_content)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 5,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AmanotesColors.Primary,
                        focusedLabelColor = AmanotesColors.Primary
                    )
                )
                
                ExposedDropdownMenuBox(
                    expanded = showCategoryDropdown,
                    onExpandedChange = { showCategoryDropdown = it }
                ) {
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.category)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showCategoryDropdown) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AmanotesColors.Primary,
                            focusedLabelColor = AmanotesColors.Primary
                        )
                    )
                    
                    ExposedDropdownMenu(
                        expanded = showCategoryDropdown,
                        onDismissRequest = { showCategoryDropdown = false }
                    ) {
                        (listOf("General") + categories).distinct().forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category) },
                                onClick = {
                                    selectedCategory = category
                                    showCategoryDropdown = false
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
private fun NoteDetailDialog(
    note: NoteEntity,
    onDismiss: () -> Unit,
    onEdit: (NoteEntity) -> Unit,
    onDelete: (NoteEntity) -> Unit,
    categories: List<String>
) {
    var isEditing by remember { mutableStateOf(false) }
    var editTitle by remember { mutableStateOf(note.title) }
    var editContent by remember { mutableStateOf(note.content) }
    var editCategory by remember { mutableStateOf(note.category) }
    var showCategoryDropdown by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            if (isEditing) {
                PremiumButton(
                    text = "Save Changes",
                    onClick = {
                        onEdit(note.copy(
                            title = editTitle.trim(),
                            content = editContent.trim(),
                            category = editCategory
                        ))
                        isEditing = false
                    }
                )
            } else {
                PremiumButton(
                    text = "Edit",
                    onClick = { isEditing = true }
                )
            }
        },
        dismissButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (!isEditing) {
                    PremiumButton(
                        text = "Delete",
                        onClick = { onDelete(note) },
                        variant = ButtonVariant.Outlined
                    )
                }
                PremiumButton(
                    text = if (isEditing) "Cancel" else "Close",
                    onClick = {
                        if (isEditing) {
                            isEditing = false
                            editTitle = note.title
                            editContent = note.content
                            editCategory = note.category
                        } else {
                            onDismiss()
                        }
                    },
                    variant = ButtonVariant.Outlined
                )
            }
        },
        title = {
            Text(
                if (isEditing) "Edit Note" else note.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                maxLines = if (isEditing) 1 else 2,
                overflow = TextOverflow.Ellipsis
            )
        },
        text = {
            if (isEditing) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        value = editTitle,
                        onValueChange = { editTitle = it },
                        label = { Text(stringResource(R.string.note_title)) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AmanotesColors.Primary,
                            focusedLabelColor = AmanotesColors.Primary
                        )
                    )
                    
                    OutlinedTextField(
                        value = editContent,
                        onValueChange = { editContent = it },
                        label = { Text(stringResource(R.string.note_content)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        maxLines = 6,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AmanotesColors.Primary,
                            focusedLabelColor = AmanotesColors.Primary
                        )
                    )
                    
                    ExposedDropdownMenuBox(
                        expanded = showCategoryDropdown,
                        onExpandedChange = { showCategoryDropdown = it }
                    ) {
                        OutlinedTextField(
                            value = editCategory,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(stringResource(R.string.category)) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showCategoryDropdown) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AmanotesColors.Primary,
                                focusedLabelColor = AmanotesColors.Primary
                            )
                        )
                        
                        ExposedDropdownMenu(
                            expanded = showCategoryDropdown,
                            onDismissRequest = { showCategoryDropdown = false }
                        ) {
                            (listOf("General") + categories).distinct().forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category) },
                                    onClick = {
                                        editCategory = category
                                        showCategoryDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        text = note.content,
                        style = MaterialTheme.typography.bodyLarge,
                        color = AmanotesColors.OnSurface
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatusChip(text = note.category, status = ChipStatus.Info)
                        Text(
                            text = "Updated ${SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(note.updatedAt))}",
                            style = MaterialTheme.typography.bodySmall,
                            color = AmanotesColors.OnSurfaceVariant
                        )
                    }
                }
            }
        },
        containerColor = AmanotesColors.Surface,
        shape = RoundedCornerShape(16.dp)
    )
}

// Helper Components
@Composable
private fun NoteCard(
    note: NoteEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    PremiumCard(
        onClick = onClick,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AmanotesColors.OnSurface,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (note.isFavorite) {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = "Favorite",
                        tint = AmanotesColors.Warning,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            
            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium,
                color = AmanotesColors.OnSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusChip(text = note.category, status = ChipStatus.Info)
                Text(
                    text = SimpleDateFormat("MMM dd", Locale.getDefault()).format(Date(note.updatedAt)),
                    style = MaterialTheme.typography.bodySmall,
                    color = AmanotesColors.OnSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun NoteListItem(
    note: NoteEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    PremiumCard(
        onClick = onClick,
        modifier = modifier
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            supportingContent = {
                Text(
                    text = note.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AmanotesColors.OnSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            },
            trailingContent = {
                Column(horizontalAlignment = Alignment.End) {
                    if (note.isFavorite) {
                        Icon(
                            Icons.Default.Favorite,
                            contentDescription = "Favorite",
                            tint = AmanotesColors.Warning,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = SimpleDateFormat("MMM dd", Locale.getDefault()).format(Date(note.updatedAt)),
                        style = MaterialTheme.typography.bodySmall,
                        color = AmanotesColors.OnSurfaceVariant
                    )
                }
            },
            leadingContent = {
                Surface(
                    modifier = Modifier.size(12.dp),
                    shape = RoundedCornerShape(6.dp),
                    color = AmanotesColors.Primary
                ) {}
            }
        )
    }
}

@Composable
private fun EmptyNotesState(
    searchQuery: String,
    selectedCategory: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Note,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = AmanotesColors.OnSurfaceVariant.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = when {
                searchQuery.isNotEmpty() -> "No notes found"
                selectedCategory != "All" -> "No notes in this category"
                else -> "No notes yet"
            },
            style = MaterialTheme.typography.titleMedium,
            color = AmanotesColors.OnSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = when {
                searchQuery.isNotEmpty() -> "Try a different search term"
                selectedCategory != "All" -> "Create a note in this category"
                else -> "Tap the + button to create your first note"
            },
            style = MaterialTheme.typography.bodyMedium,
            color = AmanotesColors.OnSurfaceVariant.copy(alpha = 0.7f)
        )
    }
}
