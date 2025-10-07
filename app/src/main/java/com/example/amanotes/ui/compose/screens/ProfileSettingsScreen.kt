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
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.amanotes.di.ServiceLocator
import com.example.amanotes.ui.compose.components.*
import com.example.amanotes.ui.compose.theme.AmanotesColors
import com.example.amanotes.ui.settings.SettingsViewModel
import com.example.amanotes.ui.settings.*
import kotlinx.coroutines.launch

@Composable
fun ProfileSettingsScreen(
    darkMode: Boolean,
    appTheme: com.example.amanotes.ui.compose.theme.AppTheme,
    onToggleDark: () -> Unit,
    onThemeChange: (com.example.amanotes.ui.compose.theme.AppTheme) -> Unit,
    onBack: () -> Unit,
    onNavigateToAuth: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    // Initialize ViewModel
    val userPreferences = remember { ServiceLocator.provideUserPreferences(context) }
    val authRepository = remember { ServiceLocator.provideAuthRepository(context) }
    val viewModel = remember { SettingsViewModel(userPreferences, authRepository) }
    
    // Collect states
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val profileName by viewModel.profileName.collectAsStateWithLifecycle(initialValue = "")
    val profileTitle by viewModel.profileTitle.collectAsStateWithLifecycle(initialValue = "")
    val profileEmail by viewModel.profileEmail.collectAsStateWithLifecycle(initialValue = "")
    val profileBio by viewModel.profileBio.collectAsStateWithLifecycle(initialValue = "")
    
    // Settings states (read-only from ViewModel)
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsStateWithLifecycle(initialValue = true)
    val soundEnabled by viewModel.soundEnabled.collectAsStateWithLifecycle(initialValue = true)
    val vibrationEnabled by viewModel.vibrationEnabled.collectAsStateWithLifecycle(initialValue = true)
    val autoSync by viewModel.autoSync.collectAsStateWithLifecycle(initialValue = true)
    val wifiOnlySync by viewModel.wifiOnlySync.collectAsStateWithLifecycle(initialValue = false)
    val analyticsEnabled by viewModel.analyticsEnabled.collectAsStateWithLifecycle(initialValue = true)
    val crashReporting by viewModel.crashReporting.collectAsStateWithLifecycle(initialValue = true)
    val biometricAuth by viewModel.biometricAuth.collectAsStateWithLifecycle(initialValue = false)
    val autoBackup by viewModel.autoBackup.collectAsStateWithLifecycle(initialValue = true)
    
    // Dialog states
    var showEditProfile by remember { mutableStateOf(false) }
    var showThemeSelector by remember { mutableStateOf(false) }
    var showNotificationSettings by remember { mutableStateOf(false) }
    var showPrivacySettings by remember { mutableStateOf(false) }
    var showSecuritySettings by remember { mutableStateOf(false) }
    var showDataManagement by remember { mutableStateOf(false) }
    var showAnalytics by remember { mutableStateOf(false) }
    var showAbout by remember { mutableStateOf(false) }
    var showSignOutDialog by remember { mutableStateOf(false) }
    var showDeleteAccountDialog by remember { mutableStateOf(false) }
    
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600
    val isLandscape = configuration.screenWidthDp > configuration.screenHeightDp
    
    // Handle navigation
    LaunchedEffect(uiState.shouldNavigateToAuth) {
        if (uiState.shouldNavigateToAuth) {
            onNavigateToAuth()
            viewModel.onNavigatedToAuth()
        }
    }
    
    // Show messages
    uiState.message?.let { message ->
        LaunchedEffect(message) {
            // In a real app, you'd show a snackbar here
            viewModel.clearMessage()
        }
    }
    
    uiState.error?.let { error ->
        LaunchedEffect(error) {
            // In a real app, you'd show an error snackbar here
            viewModel.clearMessage()
        }
    }

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
                        userName = profileName.ifEmpty { "Scholar" },
                        userTitle = profileTitle.ifEmpty { "Academy Member" },
                        userEmail = profileEmail.ifEmpty { "scholar@academy.edu" },
                        userBio = profileBio.ifEmpty { "Passionate about learning and knowledge discovery." },
                        onEditClick = { showEditProfile = true }
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    AcademicStatsSection()
                }
                
                // Right column - Settings
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                           QuickSettingsSection(
                               darkMode = darkMode,
                               appTheme = appTheme,
                               notificationsEnabled = notificationsEnabled,
                               soundEnabled = soundEnabled,
                               autoSync = autoSync,
                               onToggleDark = onToggleDark,
                               onShowThemeSelector = { showThemeSelector = true },
                               onToggleNotifications = viewModel::toggleNotifications,
                               onToggleSound = viewModel::toggleSound,
                               onToggleAutoSync = viewModel::toggleAutoSync
                           )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    SettingsCategoriesSection(
                        onNotificationSettings = { showNotificationSettings = true },
                        onPrivacySettings = { showPrivacySettings = true },
                        onSecuritySettings = { showSecuritySettings = true },
                        onDataManagement = { showDataManagement = true },
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
                        userName = profileName.ifEmpty { "Scholar" },
                        userTitle = profileTitle.ifEmpty { "Academy Member" },
                        userEmail = profileEmail.ifEmpty { "scholar@academy.edu" },
                        userBio = profileBio.ifEmpty { "Passionate about learning and knowledge discovery." },
                        onEditClick = { showEditProfile = true }
                    )
                }
                
                item {
                    AcademicStatsSection()
                }
                
                // Quick Settings
                item {
                           QuickSettingsSection(
                               darkMode = darkMode,
                               appTheme = appTheme,
                               notificationsEnabled = notificationsEnabled,
                               soundEnabled = soundEnabled,
                               autoSync = autoSync,
                               onToggleDark = onToggleDark,
                               onShowThemeSelector = { showThemeSelector = true },
                               onToggleNotifications = viewModel::toggleNotifications,
                               onToggleSound = viewModel::toggleSound,
                               onToggleAutoSync = viewModel::toggleAutoSync
                           )
                }
                
                // Detailed Settings Categories
                item {
                    SettingsCategoriesSection(
                        onNotificationSettings = { showNotificationSettings = true },
                        onPrivacySettings = { showPrivacySettings = true },
                        onSecuritySettings = { showSecuritySettings = true },
                        onDataManagement = { showDataManagement = true },
                        onShowAnalytics = { showAnalytics = true },
                        onShowAbout = { showAbout = true }
                    )
                }
                
                item {
                    ActionsSection(
                        onSignOut = { showSignOutDialog = true },
                        onDeleteAccount = { showDeleteAccountDialog = true }
                    )
                }
            }
        }
        
        // Dialogs
               if (showEditProfile) {
                   EditProfileDialog(
                       userName = profileName,
                       userTitle = profileTitle,
                       userEmail = profileEmail,
                       userBio = profileBio,
                       onDismiss = { showEditProfile = false },
                       onSave = { newName, newTitle, newEmail, newBio ->
                           viewModel.updateProfile(newName, newTitle, newEmail, newBio)
                           showEditProfile = false
                       }
                   )
               }

               if (showThemeSelector) {
                   ThemeSelectorDialog(
                       currentTheme = appTheme,
                       onDismiss = { showThemeSelector = false },
                       onThemeSelected = { theme ->
                           onThemeChange(theme)
                           showThemeSelector = false
                       }
                   )
               }
        
        if (showNotificationSettings) {
            NotificationSettingsDialog(
                notificationsEnabled = notificationsEnabled,
                soundEnabled = soundEnabled,
                vibrationEnabled = vibrationEnabled,
                onDismiss = { showNotificationSettings = false },
                onToggleNotifications = viewModel::toggleNotifications,
                onToggleSound = viewModel::toggleSound,
                onToggleVibration = viewModel::toggleVibration
            )
        }
        
        if (showPrivacySettings) {
            PrivacySettingsDialog(
                analyticsEnabled = analyticsEnabled,
                crashReporting = crashReporting,
                onDismiss = { showPrivacySettings = false },
                onToggleAnalytics = viewModel::toggleAnalytics,
                onToggleCrashReporting = viewModel::toggleCrashReporting
            )
        }
        
        if (showSecuritySettings) {
            SecuritySettingsDialog(
                biometricAuth = biometricAuth,
                onDismiss = { showSecuritySettings = false },
                onToggleBiometric = viewModel::toggleBiometricAuth
            )
        }
        
        if (showDataManagement) {
            DataManagementDialog(
                autoBackup = autoBackup,
                wifiOnlySync = wifiOnlySync,
                onDismiss = { showDataManagement = false },
                onToggleAutoBackup = viewModel::toggleAutoBackup,
                onToggleWifiOnly = viewModel::toggleWifiOnlySync,
                onExportData = viewModel::exportData,
                onClearCache = viewModel::clearCache
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
        
        if (showSignOutDialog) {
            ConfirmationDialog(
                title = "Sign Out",
                message = "Are you sure you want to sign out of your account?",
                confirmText = "Sign Out",
                onConfirm = {
                    viewModel.signOut()
                    showSignOutDialog = false
                },
                onDismiss = { showSignOutDialog = false }
            )
        }
        
        if (showDeleteAccountDialog) {
            ConfirmationDialog(
                title = "Delete Account",
                message = "This action cannot be undone. All your data will be permanently deleted.",
                confirmText = "Delete Account",
                isDestructive = true,
                onConfirm = {
                    viewModel.deleteAccount()
                    showDeleteAccountDialog = false
                },
                onDismiss = { showDeleteAccountDialog = false }
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
                           icon = Icons.AutoMirrored.Filled.Assignment,
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
private fun QuickSettingsSection(
    darkMode: Boolean,
    appTheme: com.example.amanotes.ui.compose.theme.AppTheme,
    notificationsEnabled: Boolean,
    soundEnabled: Boolean,
    autoSync: Boolean,
    onToggleDark: () -> Unit,
    onShowThemeSelector: () -> Unit,
    onToggleNotifications: () -> Unit,
    onToggleSound: () -> Unit,
    onToggleAutoSync: () -> Unit
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
                    text = "Quick Settings",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AmanotesColors.OnSurface
                )
            }
            
                   // Toggle Settings
                   SettingToggleItem(
                       icon = Icons.Default.DarkMode,
                       title = "Dark Mode",
                       subtitle = "Enable dark interface",
                       checked = darkMode,
                       onToggle = onToggleDark
                   )

                   SettingActionItem(
                       icon = Icons.Default.Palette,
                       title = "App Theme",
                       subtitle = "Current: ${appTheme.displayName}",
                       onClick = onShowThemeSelector
                   )
            
            SettingToggleItem(
                icon = Icons.Default.Notifications,
                title = "Notifications",
                subtitle = "Study reminders and updates",
                checked = notificationsEnabled,
                onToggle = onToggleNotifications
            )
            
                   SettingToggleItem(
                       icon = Icons.AutoMirrored.Filled.VolumeUp,
                       title = "Sound Effects",
                       subtitle = "Audio feedback",
                       checked = soundEnabled,
                       onToggle = onToggleSound
                   )
            
            SettingToggleItem(
                icon = Icons.Default.Sync,
                title = "Auto Sync",
                subtitle = "Sync across devices",
                checked = autoSync,
                onToggle = onToggleAutoSync
            )
        }
    }
}

@Composable
private fun SettingsCategoriesSection(
    onNotificationSettings: () -> Unit,
    onPrivacySettings: () -> Unit,
    onSecuritySettings: () -> Unit,
    onDataManagement: () -> Unit,
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
                    Icons.Default.Tune,
                    contentDescription = null,
                    tint = AmanotesColors.Accent,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Advanced Settings",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AmanotesColors.OnSurface
                )
            }
            
            // Settings Categories
            SettingActionItem(
                icon = Icons.Default.NotificationsActive,
                title = "Notification Settings",
                subtitle = "Customize alerts and reminders",
                onClick = onNotificationSettings
            )
            
            SettingActionItem(
                icon = Icons.Default.Shield,
                title = "Privacy & Data",
                subtitle = "Control your data sharing",
                onClick = onPrivacySettings
            )
            
            SettingActionItem(
                icon = Icons.Default.Security,
                title = "Security",
                subtitle = "Biometric auth and security",
                onClick = onSecuritySettings
            )
            
            SettingActionItem(
                icon = Icons.Default.Storage,
                title = "Data Management",
                subtitle = "Backup, sync, and storage",
                onClick = onDataManagement
            )
            
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                color = AmanotesColors.OnSurfaceVariant.copy(alpha = 0.3f)
            )
            
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


