package com.example.amanotes.ui.compose.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.amanotes.ui.compose.theme.AmanotesColors

@Composable
fun PremiumCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    elevation: Int = 8,
    content: @Composable ColumnScope.() -> Unit
) {
    val clickableModifier = if (onClick != null) {
        Modifier.clickable { onClick() }
    } else Modifier

    Card(
        modifier = modifier.then(clickableModifier),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation.dp),
        colors = CardDefaults.cardColors(
            containerColor = AmanotesColors.SurfaceVariant
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(content = content)
    }
}

@Composable
fun GradientCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val clickableModifier = if (onClick != null) {
        Modifier.clickable { onClick() }
    } else Modifier

    Box(
        modifier = modifier
            .then(clickableModifier)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        AmanotesColors.GradientStart,
                        AmanotesColors.GradientEnd
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            content = content
        )
    }
}

@Composable
fun PremiumButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: ButtonVariant = ButtonVariant.Primary
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(100),
        label = "button_scale"
    )

    val backgroundBrush = when (variant) {
        ButtonVariant.Primary -> Brush.linearGradient(
            colors = listOf(AmanotesColors.Primary, AmanotesColors.Secondary)
        )
        ButtonVariant.Secondary -> Brush.linearGradient(
            colors = listOf(AmanotesColors.SurfaceVariant, AmanotesColors.SurfaceContainer)
        )
        ButtonVariant.Outlined -> Brush.linearGradient(
            colors = listOf(Color.Transparent, Color.Transparent)
        )
    }

    Box(
        modifier = modifier
            .scale(scale)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundBrush)
            .then(
                if (variant == ButtonVariant.Outlined) {
                    Modifier.border(
                        1.dp,
                        AmanotesColors.Primary,
                        RoundedCornerShape(12.dp)
                    )
                } else Modifier
            )
            .clickable(enabled = enabled) {
                isPressed = true
                onClick()
                isPressed = false
            }
            .padding(horizontal = 24.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = when (variant) {
                ButtonVariant.Primary -> AmanotesColors.OnPrimary
                ButtonVariant.Secondary -> AmanotesColors.OnSurface
                ButtonVariant.Outlined -> AmanotesColors.Primary
            },
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

enum class ButtonVariant {
    Primary, Secondary, Outlined
}

@Composable
fun PremiumFloatingActionButton(
    onClick: () -> Unit,
    icon: ImageVector = Icons.Default.Add,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        containerColor = AmanotesColors.Primary,
        contentColor = AmanotesColors.OnPrimary,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 12.dp,
            pressedElevation = 16.dp
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun StatusChip(
    text: String,
    status: ChipStatus,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (status) {
        ChipStatus.Success -> AmanotesColors.Success.copy(alpha = 0.15f)
        ChipStatus.Warning -> AmanotesColors.Warning.copy(alpha = 0.15f)
        ChipStatus.Error -> AmanotesColors.Error.copy(alpha = 0.15f)
        ChipStatus.Info -> AmanotesColors.Primary.copy(alpha = 0.15f)
        ChipStatus.Default -> AmanotesColors.SurfaceContainer
    }

    val textColor = when (status) {
        ChipStatus.Success -> AmanotesColors.Success
        ChipStatus.Warning -> AmanotesColors.Warning
        ChipStatus.Error -> AmanotesColors.Error
        ChipStatus.Info -> AmanotesColors.Primary
        ChipStatus.Default -> AmanotesColors.OnSurface
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

enum class ChipStatus {
    Success, Warning, Error, Info, Default
}

@Composable
fun PremiumProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    showPercentage: Boolean = true
) {
    Column(modifier = modifier) {
        if (showPercentage) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Progress",
                    style = MaterialTheme.typography.labelLarge,
                    color = AmanotesColors.OnSurface
                )
                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.labelLarge,
                    color = AmanotesColors.Primary,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(AmanotesColors.SurfaceContainer)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                AmanotesColors.Primary,
                                AmanotesColors.Secondary
                            )
                        )
                    )
            )
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    subtitle: String? = null,
    icon: ImageVector? = null,
    trend: TrendDirection? = null,
    modifier: Modifier = Modifier
) {
    PremiumCard(modifier = modifier) {
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
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AmanotesColors.OnSurfaceVariant
                )
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = AmanotesColors.Primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                color = AmanotesColors.OnSurface,
                fontWeight = FontWeight.Bold
            )

            if (subtitle != null || trend != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (trend != null) {
                        val trendColor = when (trend) {
                            TrendDirection.Up -> AmanotesColors.Success
                            TrendDirection.Down -> AmanotesColors.Error
                            TrendDirection.Neutral -> AmanotesColors.OnSurfaceVariant
                        }
                        Text(
                            text = when (trend) {
                                TrendDirection.Up -> "↗"
                                TrendDirection.Down -> "↘"
                                TrendDirection.Neutral -> "→"
                            },
                            color = trendColor,
                            fontSize = 14.sp
                        )
                    }
                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = AmanotesColors.OnSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

enum class TrendDirection {
    Up, Down, Neutral
}
