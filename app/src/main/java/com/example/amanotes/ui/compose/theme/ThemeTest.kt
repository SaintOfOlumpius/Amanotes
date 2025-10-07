package com.example.amanotes.ui.compose.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Test file to verify theme switching functionality
 * This demonstrates how the theme system adapts colors dynamically
 */
@Composable
fun ThemeTestPreview() {
    // Test Dark Academia theme
    AmanotesColors.current = AppTheme.DARK_ACADEMIA
    val academiaBackground = AmanotesColors.Background
    val academiaPrimary = AmanotesColors.Primary
    
    // Test Sci-Fi theme  
    AmanotesColors.current = AppTheme.SCI_FI
    val sciFiBackground = AmanotesColors.Background
    val sciFiPrimary = AmanotesColors.Primary
    
    // Verify colors are different
    assert(academiaBackground != sciFiBackground) { "Theme backgrounds should be different" }
    assert(academiaPrimary != sciFiPrimary) { "Theme primary colors should be different" }
    
    // Verify Sci-Fi colors match expected values
    assert(sciFiBackground == Color(0xFF0A0A0F)) { "Sci-Fi background should be deep space black" }
    assert(sciFiPrimary == Color(0xFF00E5FF)) { "Sci-Fi primary should be neon cyan" }
}

/**
 * Utility function to get theme display information
 */
fun getThemeInfo(theme: AppTheme): String {
    return when (theme) {
        AppTheme.DARK_ACADEMIA -> "🎓 Scholarly atmosphere with warm browns and golds"
        AppTheme.SCI_FI -> "🚀 Futuristic minimalism with neon accents and deep space blacks"
        AppTheme.LIGHT_ACADEMIA -> "☀️ Bright, airy scholarly environment with warm earth tones"
    }
}
