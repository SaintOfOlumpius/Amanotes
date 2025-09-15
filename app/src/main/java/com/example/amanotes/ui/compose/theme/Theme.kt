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

// Dark Academia Color Palette
object AmanotesColors {
    // Primary Colors - Rich burgundy and deep forest green
    val Primary = Color(0xFF8B4513) // Saddle brown
    val PrimaryVariant = Color(0xFF654321) // Dark brown
    val Secondary = Color(0xFF2F4F2F) // Dark slate gray (forest green)
    val SecondaryVariant = Color(0xFF1C3A1C) // Darker forest green

    // Surface Colors for dark academia theme
    val Surface = Color(0xFF1A1610) // Very dark brown-black
    val SurfaceVariant = Color(0xFF2D2418) // Dark sepia
    val SurfaceContainer = Color(0xFF3D3025) // Medium brown
    val SurfaceContainerHigh = Color(0xFF4A3D2E) // Lighter brown

    // Background - Deep parchment/library feel
    val Background = Color(0xFF0F0D0A) // Almost black with brown tint

    // Accent colors for dark academia
    val Accent = Color(0xFFD4AF37) // Antique gold
    val AccentVariant = Color(0xFFB8860B) // Dark goldenrod
    
    // On Colors - Warm off-whites and creams
    val OnPrimary = Color(0xFFFFF8DC) // Cornsilk
    val OnSecondary = Color(0xFFFAF0E6) // Linen
    val OnSurface = Color(0xFFF5F5DC) // Beige
    val OnSurfaceVariant = Color(0xFFD2B48C) // Tan
    val OnBackground = Color(0xFFFFFAF0) // Floral white

    // Status Colors - Muted earth tones
    val Success = Color(0xFF556B2F) // Dark olive green
    val Warning = Color(0xFFCD853F) // Peru
    val Error = Color(0xFF8B4B4B) // Dark salmon

    // Gradient Colors - Warm book/leather tones
    val GradientStart = Color(0xFF8B4513) // Saddle brown
    val GradientEnd = Color(0xFFD4AF37) // Antique gold
    
    // Additional Dark Academia Colors
    val Leather = Color(0xFF704214) // Dark leather brown
    val Parchment = Color(0xFFF7F3E9) // Old parchment
    val InkBlue = Color(0xFF2F4F4F) // Dark slate gray
    val VintageGold = Color(0xFFDAA520) // Goldenrod
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

// Dark Academia Typography - Scholarly and elegant
private val AmanotesTypography = Typography(
    displayLarge = TextStyle(
        fontWeight = FontWeight.ExtraBold,
        fontSize = 40.sp,
        lineHeight = 48.sp,
        letterSpacing = (-0.5).sp
    ),
    headlineLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.25).sp
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 30.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.15.sp
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.15.sp
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.4.sp
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.5.sp
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


