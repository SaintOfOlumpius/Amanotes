package com.example.amanotes.ui.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Legacy AmanotesColors - kept for backward compatibility
// Will be replaced by the new theme-aware AmanotesColors from AppTheme.kt

// Academia Typography - Scholarly and elegant
val AcademiaTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 40.sp,
        lineHeight = 48.sp,
        letterSpacing = (-0.5).sp
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.25).sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 30.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.15.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.15.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.4.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.5.sp
    )
)

// Sci-Fi Typography - Futuristic and clean
val SciFiTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 40.sp,
        lineHeight = 48.sp,
        letterSpacing = 2.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 1.5.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 1.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 30.sp,
        letterSpacing = 0.5.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.75.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.5.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.5.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 1.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        letterSpacing = 1.sp
    )
)

// Color scheme creators
fun createDarkAcademiaColorScheme() = darkColorScheme(
    primary = DarkAcademiaColors.Primary,
    secondary = DarkAcademiaColors.Secondary,
    tertiary = DarkAcademiaColors.Accent,
    background = DarkAcademiaColors.Background,
    surface = DarkAcademiaColors.Surface,
    surfaceVariant = DarkAcademiaColors.SurfaceVariant,
    onPrimary = DarkAcademiaColors.OnPrimary,
    onSecondary = DarkAcademiaColors.OnSecondary,
    onSurface = DarkAcademiaColors.OnSurface,
    onSurfaceVariant = DarkAcademiaColors.OnSurfaceVariant,
    onBackground = DarkAcademiaColors.OnBackground,
    error = DarkAcademiaColors.Error
)

fun createSciFiColorScheme() = darkColorScheme(
    primary = SciFiColors.Primary,
    secondary = SciFiColors.Secondary,
    tertiary = SciFiColors.Accent,
    background = SciFiColors.Background,
    surface = SciFiColors.Surface,
    surfaceVariant = SciFiColors.SurfaceVariant,
    onPrimary = SciFiColors.OnPrimary,
    onSecondary = SciFiColors.OnSecondary,
    onSurface = SciFiColors.OnSurface,
    onSurfaceVariant = SciFiColors.OnSurfaceVariant,
    onBackground = SciFiColors.OnBackground,
    error = SciFiColors.Error
)

fun createLightAcademiaColorScheme() = lightColorScheme(
    primary = LightAcademiaColors.Primary,
    secondary = LightAcademiaColors.Secondary,
    tertiary = LightAcademiaColors.Accent,
    background = LightAcademiaColors.Background,
    surface = LightAcademiaColors.Surface,
    surfaceVariant = LightAcademiaColors.SurfaceVariant,
    onPrimary = LightAcademiaColors.OnPrimary,
    onSecondary = LightAcademiaColors.OnSecondary,
    onSurface = LightAcademiaColors.OnSurface,
    onSurfaceVariant = LightAcademiaColors.OnSurfaceVariant,
    onBackground = LightAcademiaColors.OnBackground,
    error = LightAcademiaColors.Error
)

@Composable
fun AmanotesTheme(
    appTheme: AppTheme = AppTheme.DARK_ACADEMIA,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Set the current theme for AmanotesColors
    AmanotesColors.current = appTheme
    
    val colorScheme = when (appTheme) {
        AppTheme.DARK_ACADEMIA -> if (darkTheme) createDarkAcademiaColorScheme() else createLightAcademiaColorScheme()
        AppTheme.SCI_FI -> createSciFiColorScheme()
        AppTheme.LIGHT_ACADEMIA -> createLightAcademiaColorScheme()
    }
    
    val typography = when (appTheme) {
        AppTheme.SCI_FI -> SciFiTypography
        else -> AcademiaTypography
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}


