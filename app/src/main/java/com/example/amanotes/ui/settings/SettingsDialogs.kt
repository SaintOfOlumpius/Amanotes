package com.example.amanotes.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.amanotes.ui.compose.components.ButtonVariant
import com.example.amanotes.ui.compose.components.PremiumButton
import com.example.amanotes.ui.compose.theme.AmanotesColors
import com.example.amanotes.ui.compose.theme.AppTheme

@Composable
fun NotificationSettingsDialog(
    notificationsEnabled: Boolean,
    soundEnabled: Boolean,
    vibrationEnabled: Boolean,
    onDismiss: () -> Unit,
    onToggleNotifications: () -> Unit,
    onToggleSound: () -> Unit,
    onToggleVibration: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            PremiumButton(
                text = "Done",
                onClick = onDismiss
            )
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.NotificationsActive,
                    contentDescription = null,
                    tint = AmanotesColors.Primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Notification Settings",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = AmanotesColors.OnSurface
                )
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SettingToggleRow(
                    icon = Icons.Default.Notifications,
                    title = "Enable Notifications",
                    subtitle = "Receive study reminders and updates",
                    checked = notificationsEnabled,
                    onToggle = onToggleNotifications
                )
                
                SettingToggleRow(
                    icon = Icons.AutoMirrored.Filled.VolumeUp,
                    title = "Sound",
                    subtitle = "Play notification sounds",
                    checked = soundEnabled,
                    onToggle = onToggleSound,
                    enabled = notificationsEnabled
                )
                
                SettingToggleRow(
                    icon = Icons.Default.Vibration,
                    title = "Vibration",
                    subtitle = "Vibrate for notifications",
                    checked = vibrationEnabled,
                    onToggle = onToggleVibration,
                    enabled = notificationsEnabled
                )
            }
        },
        containerColor = AmanotesColors.Surface,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun PrivacySettingsDialog(
    analyticsEnabled: Boolean,
    crashReporting: Boolean,
    onDismiss: () -> Unit,
    onToggleAnalytics: () -> Unit,
    onToggleCrashReporting: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            PremiumButton(
                text = "Done",
                onClick = onDismiss
            )
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Shield,
                    contentDescription = null,
                    tint = AmanotesColors.Primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Privacy & Data",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = AmanotesColors.OnSurface
                )
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SettingToggleRow(
                    icon = Icons.Default.Analytics,
                    title = "Usage Analytics",
                    subtitle = "Help improve the app with anonymous usage data",
                    checked = analyticsEnabled,
                    onToggle = onToggleAnalytics
                )
                
                SettingToggleRow(
                    icon = Icons.Default.BugReport,
                    title = "Crash Reporting",
                    subtitle = "Automatically send crash reports to help fix issues",
                    checked = crashReporting,
                    onToggle = onToggleCrashReporting
                )
                
                Text(
                    text = "We respect your privacy. All data is anonymized and used only to improve the app experience.",
                    style = MaterialTheme.typography.bodySmall,
                    color = AmanotesColors.OnSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        },
        containerColor = AmanotesColors.Surface,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun SecuritySettingsDialog(
    biometricAuth: Boolean,
    onDismiss: () -> Unit,
    onToggleBiometric: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            PremiumButton(
                text = "Done",
                onClick = onDismiss
            )
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Security,
                    contentDescription = null,
                    tint = AmanotesColors.Primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Security Settings",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = AmanotesColors.OnSurface
                )
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SettingToggleRow(
                    icon = Icons.Default.Fingerprint,
                    title = "Biometric Authentication",
                    subtitle = "Use fingerprint or face unlock",
                    checked = biometricAuth,
                    onToggle = onToggleBiometric
                )
                
                Text(
                    text = "Biometric authentication adds an extra layer of security to protect your academic data.",
                    style = MaterialTheme.typography.bodySmall,
                    color = AmanotesColors.OnSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        },
        containerColor = AmanotesColors.Surface,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun DataManagementDialog(
    autoBackup: Boolean,
    wifiOnlySync: Boolean,
    onDismiss: () -> Unit,
    onToggleAutoBackup: () -> Unit,
    onToggleWifiOnly: () -> Unit,
    onExportData: () -> Unit,
    onClearCache: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            PremiumButton(
                text = "Done",
                onClick = onDismiss
            )
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Storage,
                    contentDescription = null,
                    tint = AmanotesColors.Primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Data Management",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = AmanotesColors.OnSurface
                )
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SettingToggleRow(
                    icon = Icons.Default.Backup,
                    title = "Auto Backup",
                    subtitle = "Automatically backup your data",
                    checked = autoBackup,
                    onToggle = onToggleAutoBackup
                )
                
                SettingToggleRow(
                    icon = Icons.Default.Wifi,
                    title = "WiFi Only Sync",
                    subtitle = "Only sync when connected to WiFi",
                    checked = wifiOnlySync,
                    onToggle = onToggleWifiOnly
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PremiumButton(
                        text = "Export Data",
                        onClick = onExportData,
                        variant = ButtonVariant.Outlined,
                        modifier = Modifier.weight(1f)
                    )
                    
                    PremiumButton(
                        text = "Clear Cache",
                        onClick = onClearCache,
                        variant = ButtonVariant.Secondary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        containerColor = AmanotesColors.Surface,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    confirmText: String,
    isDestructive: Boolean = false,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            PremiumButton(
                text = confirmText,
                onClick = onConfirm,
                variant = if (isDestructive) ButtonVariant.Secondary else ButtonVariant.Primary
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
                title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = if (isDestructive) AmanotesColors.Error else AmanotesColors.OnSurface
            )
        },
        text = {
            Text(
                message,
                style = MaterialTheme.typography.bodyMedium,
                color = AmanotesColors.OnSurfaceVariant
            )
        },
        containerColor = AmanotesColors.Surface,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
private fun SettingToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onToggle: () -> Unit,
    enabled: Boolean = true
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = if (enabled) AmanotesColors.Primary else AmanotesColors.OnSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = if (enabled) AmanotesColors.OnSurface else AmanotesColors.OnSurfaceVariant
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = AmanotesColors.OnSurfaceVariant
            )
        }
        
        Switch(
            checked = checked && enabled,
            onCheckedChange = { if (enabled) onToggle() },
            enabled = enabled,
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
fun ThemeSelectorDialog(
    currentTheme: AppTheme,
    onDismiss: () -> Unit,
    onThemeSelected: (AppTheme) -> Unit
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Palette, contentDescription = null, tint = AmanotesColors.Primary, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Choose Your Theme", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, color = AmanotesColors.OnSurface)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Select the perfect aesthetic for your scholarly journey",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AmanotesColors.OnSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                AppTheme.values().forEach { theme ->
                    ThemeOptionItem(
                        theme = theme,
                        isSelected = theme == currentTheme,
                        onClick = { onThemeSelected(theme) }
                    )
                }
            }
        },
        containerColor = AmanotesColors.Surface,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
private fun ThemeOptionItem(
    theme: AppTheme,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val (icon, description, colors) = when (theme) {
        AppTheme.DARK_ACADEMIA -> Triple(
            Icons.AutoMirrored.Filled.MenuBook,
            "Rich, scholarly atmosphere with warm browns and golds",
            listOf(
                androidx.compose.ui.graphics.Color(0xFF8B7355),
                androidx.compose.ui.graphics.Color(0xFFDAA520),
                androidx.compose.ui.graphics.Color(0xFF1A1611)
            )
        )
        AppTheme.SCI_FI -> Triple(
            Icons.Default.Rocket,
            "Futuristic minimalism with neon accents and deep space blacks",
            listOf(
                androidx.compose.ui.graphics.Color(0xFF00E5FF),
                androidx.compose.ui.graphics.Color(0xFF00FFA3),
                androidx.compose.ui.graphics.Color(0xFF0A0A0F)
            )
        )
        AppTheme.LIGHT_ACADEMIA -> Triple(
            Icons.Default.WbSunny,
            "Bright, airy scholarly environment with warm earth tones",
            listOf(
                androidx.compose.ui.graphics.Color(0xFF8B7355),
                androidx.compose.ui.graphics.Color(0xFFDAA520),
                androidx.compose.ui.graphics.Color(0xFFFFFDF7)
            )
        )
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) AmanotesColors.Primary.copy(alpha = 0.1f) else AmanotesColors.SurfaceVariant
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        )
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
                tint = if (isSelected) AmanotesColors.Primary else AmanotesColors.OnSurfaceVariant,
                modifier = Modifier.size(32.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = theme.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) AmanotesColors.Primary else AmanotesColors.OnSurface
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = AmanotesColors.OnSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
                
                // Color preview
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    colors.forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(color)
                        )
                    }
                }
            }
            
            if (isSelected) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = AmanotesColors.Primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
