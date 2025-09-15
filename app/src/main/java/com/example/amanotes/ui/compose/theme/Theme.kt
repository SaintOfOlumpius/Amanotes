package com.example.amanotes.ui.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Premium Color Palette
object AmanotesColors {
    // Primary Colors - Elegant Blue/Purple gradient
    val Primary = Color(0xFF6366F1) // Indigo
    val PrimaryVariant = Color(0xFF4F46E5) // Darker indigo
    val Secondary = Color(0xFF06B6D4) // Cyan
    val SecondaryVariant = Color(0xFF0891B2) // Darker cyan
    
    // Surface Colors for dark theme
    val Surface = Color(0xFF0F172A) // Very dark blue
    val SurfaceVariant = Color(0xFF1E293B) // Dark blue-gray
    val SurfaceContainer = Color(0xFF334155) // Medium blue-gray
    
    // Background
    val Background = Color(0xFF020617) // Almost black with blue tint
    
    // On Colors
    val OnPrimary = Color(0xFFFFFFFF)
    val OnSecondary = Color(0xFFFFFFFF)
    val OnSurface = Color(0xFFF8FAFC) // Very light blue-white
    val OnSurfaceVariant = Color(0xFF94A3B8) // Light blue-gray
    val OnBackground = Color(0xFFF1F5F9) // Light gray-white
    
    // Status Colors
    val Success = Color(0xFF10B981) // Green
    val Warning = Color(0xFFF59E0B) // Amber
    val Error = Color(0xFFEF4444) // Red
    
    // Gradient Colors
    val GradientStart = Color(0xFF6366F1)
    val GradientEnd = Color(0xFF06B6D4)
}

private val DarkColors = darkColorScheme(
    primary = AmanotesColors.Primary,
    onPrimary = AmanotesColors.OnPrimary,
    primaryContainer = AmanotesColors.PrimaryVariant,
    onPrimaryContainer = AmanotesColors.OnPrimary,
    
    secondary = AmanotesColors.Secondary,
    onSecondary = AmanotesColors.OnSecondary,
    secondaryContainer = AmanotesColors.SecondaryVariant,
    onSecondaryContainer = AmanotesColors.OnSecondary,
    
    background = AmanotesColors.Background,
    onBackground = AmanotesColors.OnBackground,
    
    surface = AmanotesColors.Surface,
    onSurface = AmanotesColors.OnSurface,
    surfaceVariant = AmanotesColors.SurfaceVariant,
    onSurfaceVariant = AmanotesColors.OnSurfaceVariant,
    surfaceContainer = AmanotesColors.SurfaceContainer,
    
    error = AmanotesColors.Error,
    onError = Color.White,
    
    outline = AmanotesColors.OnSurfaceVariant.copy(alpha = 0.5f),
    outlineVariant = AmanotesColors.OnSurfaceVariant.copy(alpha = 0.3f)
)

private val LightColors = lightColorScheme(
    primary = AmanotesColors.Primary,
    onPrimary = AmanotesColors.OnPrimary,
    secondary = AmanotesColors.Secondary,
    onSecondary = AmanotesColors.OnSecondary,
    background = Color(0xFFFAFAFA),
    onBackground = Color(0xFF1A1A1A),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1A1A1A),
)

// Premium Typography
private val AmanotesTypography = Typography(
    displayLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = (-0.25).sp
    ),
    headlineLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.1.sp
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    )
)

@Composable
fun AmanotesTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = AmanotesTypography,
        content = content
    )
}


