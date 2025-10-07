package com.example.amanotes.ui.compose.theme

import androidx.compose.ui.graphics.Color

// Theme Types
enum class AppTheme(val displayName: String) {
    DARK_ACADEMIA("Dark Academia"),
    SCI_FI("Sci-Fi Futuristic"),
    LIGHT_ACADEMIA("Light Academia")
}

// Dark Academia Colors (Current)
object DarkAcademiaColors {
    val Primary = Color(0xFF8B7355)
    val Secondary = Color(0xFFB8860B)
    val Accent = Color(0xFFDAA520)
    val AccentVariant = Color(0xFF9B7E47)
    val Background = Color(0xFF1A1611)
    val Surface = Color(0xFF2D2419)
    val SurfaceVariant = Color(0xFF3D3426)
    val OnPrimary = Color(0xFFFFF8DC)
    val OnSecondary = Color(0xFF2D2419)
    val OnSurface = Color(0xFFF5E6D3)
    val OnSurfaceVariant = Color(0xFFD4C4A8)
    val OnBackground = Color(0xFFF5E6D3)
    val Success = Color(0xFF4CAF50)
    val Warning = Color(0xFFFF9800)
    val Error = Color(0xFFE57373)
}

// Sci-Fi Futuristic Colors
object SciFiColors {
    val Primary = Color(0xFF00E5FF)        // Cyan blue
    val Secondary = Color(0xFF7C4DFF)      // Purple
    val Accent = Color(0xFF00FFA3)         // Neon green
    val AccentVariant = Color(0xFF40C4FF)  // Light blue
    val Background = Color(0xFF0A0A0F)     // Deep space black
    val Surface = Color(0xFF1A1A2E)        // Dark blue-black
    val SurfaceVariant = Color(0xFF16213E)  // Darker blue
    val OnPrimary = Color(0xFF000000)      // Black on bright colors
    val OnSecondary = Color(0xFFFFFFFF)    // White
    val OnSurface = Color(0xFFE0E0E0)      // Light gray
    val OnSurfaceVariant = Color(0xFFB0B0B0) // Medium gray
    val OnBackground = Color(0xFFE0E0E0)   // Light gray
    val Success = Color(0xFF00E676)        // Neon green
    val Warning = Color(0xFFFFAB00)        // Neon orange
    val Error = Color(0xFFFF1744)          // Neon red
}

// Light Academia Colors (Bonus)
object LightAcademiaColors {
    val Primary = Color(0xFF8B7355)
    val Secondary = Color(0xFFB8860B)
    val Accent = Color(0xFFDAA520)
    val AccentVariant = Color(0xFF9B7E47)
    val Background = Color(0xFFFFFDF7)     // Warm white
    val Surface = Color(0xFFF8F6F0)        // Off-white
    val SurfaceVariant = Color(0xFFEFEDE7) // Light beige
    val OnPrimary = Color(0xFFFFFFFF)
    val OnSecondary = Color(0xFFFFFFFF)
    val OnSurface = Color(0xFF2D2419)
    val OnSurfaceVariant = Color(0xFF5D4E37)
    val OnBackground = Color(0xFF2D2419)
    val Success = Color(0xFF2E7D32)
    val Warning = Color(0xFFE65100)
    val Error = Color(0xFFC62828)
}

// Theme-aware color provider
object AmanotesColors {
    lateinit var current: AppTheme
    
    val Primary: Color get() = when (current) {
        AppTheme.DARK_ACADEMIA -> DarkAcademiaColors.Primary
        AppTheme.SCI_FI -> SciFiColors.Primary
        AppTheme.LIGHT_ACADEMIA -> LightAcademiaColors.Primary
    }
    
    val Secondary: Color get() = when (current) {
        AppTheme.DARK_ACADEMIA -> DarkAcademiaColors.Secondary
        AppTheme.SCI_FI -> SciFiColors.Secondary
        AppTheme.LIGHT_ACADEMIA -> LightAcademiaColors.Secondary
    }
    
    val Accent: Color get() = when (current) {
        AppTheme.DARK_ACADEMIA -> DarkAcademiaColors.Accent
        AppTheme.SCI_FI -> SciFiColors.Accent
        AppTheme.LIGHT_ACADEMIA -> LightAcademiaColors.Accent
    }
    
    val AccentVariant: Color get() = when (current) {
        AppTheme.DARK_ACADEMIA -> DarkAcademiaColors.AccentVariant
        AppTheme.SCI_FI -> SciFiColors.AccentVariant
        AppTheme.LIGHT_ACADEMIA -> LightAcademiaColors.AccentVariant
    }
    
    val Background: Color get() = when (current) {
        AppTheme.DARK_ACADEMIA -> DarkAcademiaColors.Background
        AppTheme.SCI_FI -> SciFiColors.Background
        AppTheme.LIGHT_ACADEMIA -> LightAcademiaColors.Background
    }
    
    val Surface: Color get() = when (current) {
        AppTheme.DARK_ACADEMIA -> DarkAcademiaColors.Surface
        AppTheme.SCI_FI -> SciFiColors.Surface
        AppTheme.LIGHT_ACADEMIA -> LightAcademiaColors.Surface
    }
    
    val SurfaceVariant: Color get() = when (current) {
        AppTheme.DARK_ACADEMIA -> DarkAcademiaColors.SurfaceVariant
        AppTheme.SCI_FI -> SciFiColors.SurfaceVariant
        AppTheme.LIGHT_ACADEMIA -> LightAcademiaColors.SurfaceVariant
    }
    
    val OnPrimary: Color get() = when (current) {
        AppTheme.DARK_ACADEMIA -> DarkAcademiaColors.OnPrimary
        AppTheme.SCI_FI -> SciFiColors.OnPrimary
        AppTheme.LIGHT_ACADEMIA -> LightAcademiaColors.OnPrimary
    }
    
    val OnSecondary: Color get() = when (current) {
        AppTheme.DARK_ACADEMIA -> DarkAcademiaColors.OnSecondary
        AppTheme.SCI_FI -> SciFiColors.OnSecondary
        AppTheme.LIGHT_ACADEMIA -> LightAcademiaColors.OnSecondary
    }
    
    val OnSurface: Color get() = when (current) {
        AppTheme.DARK_ACADEMIA -> DarkAcademiaColors.OnSurface
        AppTheme.SCI_FI -> SciFiColors.OnSurface
        AppTheme.LIGHT_ACADEMIA -> LightAcademiaColors.OnSurface
    }
    
    val OnSurfaceVariant: Color get() = when (current) {
        AppTheme.DARK_ACADEMIA -> DarkAcademiaColors.OnSurfaceVariant
        AppTheme.SCI_FI -> SciFiColors.OnSurfaceVariant
        AppTheme.LIGHT_ACADEMIA -> LightAcademiaColors.OnSurfaceVariant
    }
    
    val OnBackground: Color get() = when (current) {
        AppTheme.DARK_ACADEMIA -> DarkAcademiaColors.OnBackground
        AppTheme.SCI_FI -> SciFiColors.OnBackground
        AppTheme.LIGHT_ACADEMIA -> LightAcademiaColors.OnBackground
    }
    
    val Success: Color get() = when (current) {
        AppTheme.DARK_ACADEMIA -> DarkAcademiaColors.Success
        AppTheme.SCI_FI -> SciFiColors.Success
        AppTheme.LIGHT_ACADEMIA -> LightAcademiaColors.Success
    }
    
    val Warning: Color get() = when (current) {
        AppTheme.DARK_ACADEMIA -> DarkAcademiaColors.Warning
        AppTheme.SCI_FI -> SciFiColors.Warning
        AppTheme.LIGHT_ACADEMIA -> LightAcademiaColors.Warning
    }
    
    val Error: Color get() = when (current) {
        AppTheme.DARK_ACADEMIA -> DarkAcademiaColors.Error
        AppTheme.SCI_FI -> SciFiColors.Error
        AppTheme.LIGHT_ACADEMIA -> LightAcademiaColors.Error
    }
    
    // Additional colors for components
    val SurfaceContainer: Color get() = when (current) {
        AppTheme.DARK_ACADEMIA -> DarkAcademiaColors.SurfaceVariant
        AppTheme.SCI_FI -> SciFiColors.SurfaceVariant
        AppTheme.LIGHT_ACADEMIA -> LightAcademiaColors.SurfaceVariant
    }
    
    val GradientStart: Color get() = when (current) {
        AppTheme.DARK_ACADEMIA -> DarkAcademiaColors.Primary
        AppTheme.SCI_FI -> SciFiColors.Primary
        AppTheme.LIGHT_ACADEMIA -> LightAcademiaColors.Primary
    }
    
    val GradientEnd: Color get() = when (current) {
        AppTheme.DARK_ACADEMIA -> DarkAcademiaColors.Accent
        AppTheme.SCI_FI -> SciFiColors.Accent
        AppTheme.LIGHT_ACADEMIA -> LightAcademiaColors.Accent
    }
}
