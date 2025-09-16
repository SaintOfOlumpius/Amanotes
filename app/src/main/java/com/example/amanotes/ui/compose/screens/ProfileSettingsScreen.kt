@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.amanotes.ui.compose.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.amanotes.ui.compose.components.*
import com.example.amanotes.ui.compose.theme.AmanotesColors

@Composable
fun ProfileSettingsScreen(darkMode: Boolean, onToggleDark: () -> Unit, onBack: () -> Unit) {
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600
    val isLandscape = configuration.screenWidthDp > configuration.screenHeightDp
    
    // Profile state
    var showEditProfile by remember { mutableStateOf(false) }
    var userName by remember { mutableStateOf("Dr. Eleanor Whitmore") }
    var userTitle by remember { mutableStateOf("Distinguished Scholar") }
    var userEmail by remember { mutableStateOf("e.whitmore@academy.edu") }
    var userBio by remember { mutableStateOf("Passionate about literature, philosophy, and the pursuit of knowledge. Currently researching medieval manuscripts and their influence on modern academic thought.") }
    
    // Settings state
    var notificationsEnabled by remember { mutableStateOf(true) }
    var soundEnabled by remember { mutableStateOf(true) }
    var autoSync by remember { mutableStateOf(true) }
    var showAnalytics by remember { mutableStateOf(false) }
    var showAbout by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Scholar's Profile",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = AmanotesColors.OnSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = AmanotesColors.Primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AmanotesColors.Surface
                )
            )
        },
        containerColor = AmanotesColors.Background
    ) { padding ->
        if (isTablet && isLandscape) {
            // Tablet landscape layout
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Left column - Profile
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    ProfileSection(
                        userName = userName,
                        userTitle = userTitle,
                        userEmail = userEmail,
                        userBio = userBio,
                        onEditClick = { showEditProfile = true }
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    AcademicStatsSection()
                }
                
                // Right column - Settings
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    SettingsSection(
                        darkMode = darkMode,
                        notificationsEnabled = notificationsEnabled,
                        soundEnabled = soundEnabled,
                        autoSync = autoSync,
                        onToggleDark = onToggleDark,
                        onToggleNotifications = { notificationsEnabled = !notificationsEnabled },
                        onToggleSound = { soundEnabled = !soundEnabled },
                        onToggleAutoSync = { autoSync = !autoSync },
                        onShowAnalytics = { showAnalytics = true },
                        onShowAbout = { showAbout = true }
                    )
                }
            }
        } else {
            // Phone/tablet portrait layout
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = if (isTablet) 32.dp else 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(vertical = 20.dp)
            ) {
                item {
                    ProfileSection(
                        userName = userName,
                        userTitle = userTitle,
                        userEmail = userEmail,
                        userBio = userBio,
                        onEditClick = { showEditProfile = true }
                    )
                }
                
                item {
                    AcademicStatsSection()
                }
                
                item {
                    SettingsSection(
                        darkMode = darkMode,
                        notificationsEnabled = notificationsEnabled,
                        soundEnabled = soundEnabled,
                        autoSync = autoSync,
                        onToggleDark = onToggleDark,
                        onToggleNotifications = { notificationsEnabled = !notificationsEnabled },
                        onToggleSound = { soundEnabled = !soundEnabled },
                        onToggleAutoSync = { autoSync = !autoSync },
                        onShowAnalytics = { showAnalytics = true },
                        onShowAbout = { showAbout = true }
                    )
                }
                
                item {
                    ActionsSection(
                        onSignOut = { /* TODO: Implement sign out */ },
                        onDeleteAccount = { /* TODO: Implement account deletion */ }
                    )
                }
            }
        }
        
        // Dialogs
        if (showEditProfile) {
            EditProfileDialog(
                userName = userName,
                userTitle = userTitle,
                userEmail = userEmail,
                userBio = userBio,
                onDismiss = { showEditProfile = false },
                onSave = { newName, newTitle, newEmail, newBio ->
                    userName = newName
                    userTitle = newTitle
                    userEmail = newEmail
                    userBio = newBio
                    showEditProfile = false
                }
            )
        }
        
        if (showAnalytics) {
            AnalyticsDialog(
                onDismiss = { showAnalytics = false }
            )
        }
        
        if (showAbout) {
            AboutDialog(
                onDismiss = { showAbout = false }
            )
        }
    }
}

// Component Sections
@Composable
private fun ProfileSection(
    userName: String,
    userTitle: String,
    userEmail: String,
    userBio: String,
    onEditClick: () -> Unit
) {
    GradientCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // Profile avatar placeholder
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(AmanotesColors.AccentVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = AmanotesColors.OnPrimary,
                        modifier = Modifier.size(40.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = userName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = AmanotesColors.OnPrimary
                )
                
                Text(
                    text = userTitle,
                    style = MaterialTheme.typography.titleMedium,
                    color = AmanotesColors.OnPrimary.copy(alpha = 0.9f)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = userEmail,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AmanotesColors.OnPrimary.copy(alpha = 0.8f)
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = userBio,
                    style = MaterialTheme.typography.bodySmall,
                    color = AmanotesColors.OnPrimary.copy(alpha = 0.7f),
                    lineHeight = MaterialTheme.typography.bodySmall.lineHeight
                )
            }
            
            IconButton(
                onClick = onEditClick,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(AmanotesColors.OnPrimary.copy(alpha = 0.2f))
            ) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Edit Profile",
                    tint = AmanotesColors.OnPrimary
                )
            }
        }
    }
}

@Composable
private fun AcademicStatsSection() {
    PremiumCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    Icons.Default.School,
                    contentDescription = null,
                    tint = AmanotesColors.Accent,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Academic Progress",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AmanotesColors.OnSurface
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    icon = Icons.Default.Assignment,
                    label = "Notes",
                    value = "47",
                    color = AmanotesColors.Primary
                )
                StatItem(
                    icon = Icons.Default.Folder,
                    label = "Projects",
                    value = "12",
                    color = AmanotesColors.Secondary
                )
                StatItem(
                    icon = Icons.Default.CheckCircle,
                    label = "Completed",
                    value = "89%",
                    color = AmanotesColors.Success
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    icon: ImageVector,
    label: String,
    value: String,
    color: androidx.compose.ui.graphics.Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = AmanotesColors.OnSurface
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = AmanotesColors.OnSurfaceVariant
        )
    }
}

@Composable
private fun SettingsSection(
    darkMode: Boolean,
    notificationsEnabled: Boolean,
    soundEnabled: Boolean,
    autoSync: Boolean,
    onToggleDark: () -> Unit,
    onToggleNotifications: () -> Unit,
    onToggleSound: () -> Unit,
    onToggleAutoSync: () -> Unit,
    onShowAnalytics: () -> Unit,
    onShowAbout: () -> Unit
) {
    PremiumCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = null,
                    tint = AmanotesColors.Accent,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Academy Settings",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AmanotesColors.OnSurface
                )
            }
            
            // Toggle Settings
            SettingToggleItem(
                icon = Icons.Default.DarkMode,
                title = "Dark Academia Mode",
                subtitle = "Elegant scholarly theme",
                checked = darkMode,
                onToggle = onToggleDark
            )
            
            SettingToggleItem(
                icon = Icons.Default.Notifications,
                title = "Scholarly Notifications",
                subtitle = "Study reminders and updates",
                checked = notificationsEnabled,
                onToggle = onToggleNotifications
            )
            
            SettingToggleItem(
                icon = Icons.Default.VolumeUp,
                title = "Sound Effects",
                subtitle = "Academic audio feedback",
                checked = soundEnabled,
                onToggle = onToggleSound
            )
            
            SettingToggleItem(
                icon = Icons.Default.Sync,
                title = "Auto Synchronization",
                subtitle = "Sync across devices",
                checked = autoSync,
                onToggle = onToggleAutoSync
            )
            
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                color = AmanotesColors.OnSurfaceVariant.copy(alpha = 0.3f)
            )
            
            // Action Items
            SettingActionItem(
                icon = Icons.Default.Analytics,
                title = "Academic Analytics",
                subtitle = "View your study patterns",
                onClick = onShowAnalytics
            )
            
            SettingActionItem(
                icon = Icons.AutoMirrored.Filled.Help,
                title = "About Academy",
                subtitle = "Version and information",
                onClick = onShowAbout
            )
        }
    }
}

@Composable
private fun SettingToggleItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = AmanotesColors.Primary,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = AmanotesColors.OnSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = AmanotesColors.OnSurfaceVariant
            )
        }
        
        Switch(
            checked = checked,
            onCheckedChange = { onToggle() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = AmanotesColors.Accent,
                checkedTrackColor = AmanotesColors.Primary,
                uncheckedThumbColor = AmanotesColors.OnSurfaceVariant,
                uncheckedTrackColor = AmanotesColors.SurfaceVariant
            )
        )
    }
}

@Composable
private fun SettingActionItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    PremiumCard(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = 4
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = AmanotesColors.Secondary,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = AmanotesColors.OnSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = AmanotesColors.OnSurfaceVariant
                )
            }
            
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = "Open",
                tint = AmanotesColors.OnSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun ActionsSection(
    onSignOut: () -> Unit,
    onDeleteAccount: () -> Unit
) {
    PremiumCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Account Actions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = AmanotesColors.OnSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            PremiumButton(
                text = "Sign Out of Academy",
                onClick = onSignOut,
                variant = ButtonVariant.Outlined,
                modifier = Modifier.fillMaxWidth()
            )
            
            PremiumButton(
                text = "Delete Account",
                onClick = onDeleteAccount,
                variant = ButtonVariant.Secondary,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// Dialog Components
@Composable
private fun EditProfileDialog(
    userName: String,
    userTitle: String,
    userEmail: String,
    userBio: String,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String) -> Unit
) {
    var editName by remember { mutableStateOf(userName) }
    var editTitle by remember { mutableStateOf(userTitle) }
    var editEmail by remember { mutableStateOf(userEmail) }
    var editBio by remember { mutableStateOf(userBio) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            PremiumButton(
                text = "Save Changes",
                onClick = { onSave(editName, editTitle, editEmail, editBio) },
                enabled = editName.isNotBlank() && editEmail.isNotBlank()
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
                "Edit Scholar Profile",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = AmanotesColors.OnSurface
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = editName,
                    onValueChange = { editName = it },
                    label = { Text("Scholar Name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AmanotesColors.Primary,
                        focusedLabelColor = AmanotesColors.Primary
                    )
                )
                
                OutlinedTextField(
                    value = editTitle,
                    onValueChange = { editTitle = it },
                    label = { Text("Academic Title") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AmanotesColors.Primary,
                        focusedLabelColor = AmanotesColors.Primary
                    )
                )
                
                OutlinedTextField(
                    value = editEmail,
                    onValueChange = { editEmail = it },
                    label = { Text("Academy Email") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AmanotesColors.Primary,
                        focusedLabelColor = AmanotesColors.Primary
                    )
                )
                
                OutlinedTextField(
                    value = editBio,
                    onValueChange = { editBio = it },
                    label = { Text("Academic Bio") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 4,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AmanotesColors.Primary,
                        focusedLabelColor = AmanotesColors.Primary
                    )
                )
            }
        },
        containerColor = AmanotesColors.Surface,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
private fun AnalyticsDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            PremiumButton(
                text = "Close",
                onClick = onDismiss
            )
        },
        title = {
            Text(
                "📊 Academic Analytics",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = AmanotesColors.OnSurface
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AnalyticItem("Study Sessions This Week", "23 hours")
                AnalyticItem("Notes Created", "47 documents")
                AnalyticItem("Projects Completed", "12 manuscripts")
                AnalyticItem("Knowledge Points", "1,847 earned")
                AnalyticItem("Reading Streak", "15 days")
                AnalyticItem("Focus Time", "4.2 hours avg")
            }
        },
        containerColor = AmanotesColors.Surface,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
private fun AnalyticItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = AmanotesColors.OnSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = AmanotesColors.OnSurface
        )
    }
}

@Composable
private fun AboutDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            PremiumButton(
                text = "Close",
                onClick = onDismiss
            )
        },
        title = {
            Text(
                "🎓 About Amanotes Academy",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = AmanotesColors.OnSurface
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Version 1.0.0 - Scholar's Edition",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = AmanotesColors.OnSurface
                )
                
                Text(
                    text = "A comprehensive digital companion for scholars, researchers, and knowledge seekers. Designed with dark academia aesthetics to inspire deep thinking and scholarly pursuit.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AmanotesColors.OnSurfaceVariant
                )
                
                Text(
                    text = "Created with passion for learning and the pursuit of knowledge.",
                    style = MaterialTheme.typography.bodySmall,
                    color = AmanotesColors.OnSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        containerColor = AmanotesColors.Surface,
        shape = RoundedCornerShape(16.dp)
    )
}


