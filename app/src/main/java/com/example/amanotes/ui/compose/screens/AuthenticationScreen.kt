package com.example.amanotes.ui.compose.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.amanotes.ui.compose.components.ButtonVariant
import com.example.amanotes.ui.compose.components.PremiumButton
import com.example.amanotes.ui.compose.theme.AmanotesColors
import kotlinx.coroutines.launch

@Composable
fun AuthenticationScreen(onAuthenticated: () -> Unit) {
    var isLogin by remember { mutableStateOf(true) }
    val offsetX = remember { Animatable(0f) }
    val thresholdPx = 140f
    val scope = rememberCoroutineScope()

    // Form state
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            AmanotesColors.Background,
            AmanotesColors.Surface,
            AmanotesColors.Background
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App branding
            Text(
                text = "Welcome to\nAmanotes",
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold,
                color = AmanotesColors.OnBackground,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Your premium notes & productivity app",
                style = MaterialTheme.typography.titleMedium,
                color = AmanotesColors.OnSurfaceVariant,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Toggle buttons
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(AmanotesColors.SurfaceContainer)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                PremiumButton(
                    text = "Login",
                    onClick = { isLogin = true },
                    variant = if (isLogin) ButtonVariant.Primary else ButtonVariant.Secondary,
                    modifier = Modifier.weight(1f)
                )
                PremiumButton(
                    text = "Sign Up",
                    onClick = { isLogin = false },
                    variant = if (!isLogin) ButtonVariant.Primary else ButtonVariant.Secondary,
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Auth form
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
                colors = CardDefaults.cardColors(containerColor = AmanotesColors.Surface),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = if (isLogin) "Welcome back!" else "Create your account",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = AmanotesColors.OnSurface
                    )
                    
                    if (!isLogin) {
                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            label = { Text("Full Name") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AmanotesColors.Primary,
                                focusedLabelColor = AmanotesColors.Primary
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                    
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AmanotesColors.Primary,
                            focusedLabelColor = AmanotesColors.Primary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = if (passwordVisible) "Hide password" else "Show password"
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AmanotesColors.Primary,
                            focusedLabelColor = AmanotesColors.Primary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    
                    if (!isLogin) {
                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = { Text("Confirm Password") },
                            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                    Icon(
                                        imageVector = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                        contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password"
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AmanotesColors.Primary,
                                focusedLabelColor = AmanotesColors.Primary
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    PremiumButton(
                        text = if (isLogin) "Sign In" else "Create Account",
                        onClick = { onAuthenticated() },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    if (isLogin) {
                        TextButton(
                            onClick = { /* Forgot password */ },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "Forgot your password?",
                                color = AmanotesColors.Primary
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Skip option
            TextButton(onClick = { onAuthenticated() }) {
                Text(
                    "Skip for now",
                    color = AmanotesColors.OnSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}


