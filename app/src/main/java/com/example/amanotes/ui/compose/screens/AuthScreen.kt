@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.amanotes.ui.compose.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.livedata.observeAsState
import com.example.amanotes.di.ServiceLocator
import com.example.amanotes.ui.auth.LoginViewModel
import com.example.amanotes.ui.auth.SignupViewModel
import com.example.amanotes.ui.auth.ViewModelFactories
import com.example.amanotes.ui.compose.components.*
import com.example.amanotes.ui.compose.theme.AmanotesColors
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(onAuthSuccess: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    // ViewModels
    val authRepository = remember { ServiceLocator.provideAuthRepository(context) }
    val loginViewModel = remember { ViewModelFactories.createLoginViewModel(authRepository) }
    val signupViewModel = remember { ViewModelFactories.createSignupViewModel(authRepository) }
    
    // State
    var showLogin by remember { mutableStateOf(true) }
    var isTransitioning by remember { mutableStateOf(false) }
    
    // Form states
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    
    // Observe auth states
    val loginLoading by loginViewModel.loading.observeAsState(false)
    val loginError by loginViewModel.error.observeAsState()
    val loginSuccess by loginViewModel.success.observeAsState(false)
    
    val signupLoading by signupViewModel.loading.observeAsState(false)
    val signupError by signupViewModel.error.observeAsState()
    val signupSuccess by signupViewModel.success.observeAsState(false)
    
    // Handle successful auth
    LaunchedEffect(loginSuccess, signupSuccess) {
        if (loginSuccess || signupSuccess) {
            onAuthSuccess()
        }
    }
    
    // Animation values for card switching
    val loginAlpha by animateFloatAsState(
        targetValue = if (showLogin) 1f else 0f,
        animationSpec = tween(400),
        label = "login_alpha"
    )
    
    val signupAlpha by animateFloatAsState(
        targetValue = if (!showLogin) 1f else 0f,
        animationSpec = tween(400),
        label = "signup_alpha"
    )
    
    // Dark academia background
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
        // Background elements - vintage library aesthetic
        VintageBackgroundElements()
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App title with dark academia styling
            Text(
                text = "Amanotes",
                style = MaterialTheme.typography.displayLarge,
                color = AmanotesColors.Accent,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp
            )
            
            Text(
                text = "Scholar's Companion",
                style = MaterialTheme.typography.titleMedium,
                color = AmanotesColors.OnSurfaceVariant,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            // Toggle buttons for switching between login and signup
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                PremiumButton(
                    text = "Scholar's Entry",
                    onClick = { showLogin = true },
                    variant = if (showLogin) ButtonVariant.Primary else ButtonVariant.Outlined,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                PremiumButton(
                    text = "Join Academy",
                    onClick = { showLogin = false },
                    variant = if (!showLogin) ButtonVariant.Primary else ButtonVariant.Outlined,
                    modifier = Modifier.weight(1f)
                )
            }
            
            // Single card container with fade animation
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(480.dp)
            ) {
                // Login card
                if (showLogin) {
                    LoginCard(
                        email = email,
                        password = password,
                        passwordVisible = passwordVisible,
                        isLoading = loginLoading,
                        errorMessage = loginError,
                        onEmailChange = { email = it },
                        onPasswordChange = { password = it },
                        onPasswordVisibilityToggle = { passwordVisible = !passwordVisible },
                        onLogin = {
                            scope.launch {
                                loginViewModel.login(email, password)
                            }
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer(alpha = loginAlpha)
                    )
                }
                
                // Signup card
                if (!showLogin) {
                    SignupCard(
                        email = email,
                        password = password,
                        confirmPassword = confirmPassword,
                        fullName = fullName,
                        passwordVisible = passwordVisible,
                        confirmPasswordVisible = confirmPasswordVisible,
                        isLoading = signupLoading,
                        errorMessage = signupError,
                        onEmailChange = { email = it },
                        onPasswordChange = { password = it },
                        onConfirmPasswordChange = { confirmPassword = it },
                        onFullNameChange = { fullName = it },
                        onPasswordVisibilityToggle = { passwordVisible = !passwordVisible },
                        onConfirmPasswordVisibilityToggle = { confirmPasswordVisible = !confirmPasswordVisible },
                        onSignup = {
                            scope.launch {
                                signupViewModel.signup(fullName, email, password)
                            }
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer(alpha = signupAlpha)
                    )
                }
            }
            
            // Testing bypass section
            Spacer(modifier = Modifier.height(16.dp))
            
            PremiumCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 6
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🧪 Testing Mode",
                        style = MaterialTheme.typography.titleSmall,
                        color = AmanotesColors.Warning,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Skip authentication for testing",
                        style = MaterialTheme.typography.bodySmall,
                        color = AmanotesColors.OnSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    PremiumButton(
                        text = "Bypass Login",
                        onClick = { onAuthSuccess() },
                        variant = ButtonVariant.Secondary,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun VintageBackgroundElements() {
    // Add subtle vintage patterns or decorative elements
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        AmanotesColors.Accent.copy(alpha = 0.1f),
                        Color.Transparent
                    ),
                    radius = 800f
                )
            )
    )
}

@Composable
private fun LoginCard(
    email: String,
    password: String,
    passwordVisible: Boolean,
    isLoading: Boolean,
    errorMessage: String?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityToggle: () -> Unit,
    onLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    PremiumCard(
        modifier = modifier
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = AmanotesColors.Accent.copy(alpha = 0.3f)
            ),
        elevation = 16
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Login header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Icon(
                    Icons.Default.MenuBook,
                    contentDescription = null,
                    tint = AmanotesColors.Accent,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(end = 12.dp)
                )
                Text(
                    text = "Scholar's Entry",
                    style = MaterialTheme.typography.headlineMedium,
                    color = AmanotesColors.OnSurface,
                    fontWeight = FontWeight.Bold
                )
            }
            
            // Email field
            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                label = { Text("Academy Email") },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AmanotesColors.Accent,
                    focusedLabelColor = AmanotesColors.Accent,
                    focusedLeadingIconColor = AmanotesColors.Accent
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Password field
            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = { Text("Secret Phrase") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null)
                },
                trailingIcon = {
                    IconButton(onClick = onPasswordVisibilityToggle) {
                        Icon(
                            if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                },
                visualTransformation = if (passwordVisible) {
                    androidx.compose.ui.text.input.VisualTransformation.None
                } else {
                    androidx.compose.ui.text.input.PasswordVisualTransformation()
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AmanotesColors.Accent,
                    focusedLabelColor = AmanotesColors.Accent,
                    focusedLeadingIconColor = AmanotesColors.Accent
                )
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Error message
            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = AmanotesColors.Error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            
            // Login button
            PremiumButton(
                text = if (isLoading) "Entering..." else "Enter Academy",
                onClick = onLogin,
                enabled = !isLoading && email.isNotBlank() && password.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun SignupCard(
    email: String,
    password: String,
    confirmPassword: String,
    fullName: String,
    passwordVisible: Boolean,
    confirmPasswordVisible: Boolean,
    isLoading: Boolean,
    errorMessage: String?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onFullNameChange: (String) -> Unit,
    onPasswordVisibilityToggle: () -> Unit,
    onConfirmPasswordVisibilityToggle: () -> Unit,
    onSignup: () -> Unit,
    modifier: Modifier = Modifier
) {
    PremiumCard(
        modifier = modifier
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = AmanotesColors.Secondary.copy(alpha = 0.3f)
            ),
        elevation = 12
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Signup header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Icon(
                    Icons.Default.School,
                    contentDescription = null,
                    tint = AmanotesColors.Secondary,
                    modifier = Modifier
                        .size(28.dp)
                        .padding(end = 8.dp)
                )
                Text(
                    text = "Join the Academy",
                    style = MaterialTheme.typography.headlineSmall,
                    color = AmanotesColors.OnSurface,
                    fontWeight = FontWeight.Bold
                )
            }
            
            // Full name field
            OutlinedTextField(
                value = fullName,
                onValueChange = onFullNameChange,
                label = { Text("Scholar Name") },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AmanotesColors.Secondary,
                    focusedLabelColor = AmanotesColors.Secondary,
                    focusedLeadingIconColor = AmanotesColors.Secondary
                )
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Email field
            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                label = { Text("Academy Email") },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AmanotesColors.Secondary,
                    focusedLabelColor = AmanotesColors.Secondary,
                    focusedLeadingIconColor = AmanotesColors.Secondary
                )
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Password field
            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = { Text("Secret Phrase") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null)
                },
                trailingIcon = {
                    IconButton(onClick = onPasswordVisibilityToggle) {
                        Icon(
                            if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                },
                visualTransformation = if (passwordVisible) {
                    androidx.compose.ui.text.input.VisualTransformation.None
                } else {
                    androidx.compose.ui.text.input.PasswordVisualTransformation()
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AmanotesColors.Secondary,
                    focusedLabelColor = AmanotesColors.Secondary,
                    focusedLeadingIconColor = AmanotesColors.Secondary
                )
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Confirm password field
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = onConfirmPasswordChange,
                label = { Text("Confirm Phrase") },
                leadingIcon = {
                    Icon(Icons.Default.LockOpen, contentDescription = null)
                },
                trailingIcon = {
                    IconButton(onClick = onConfirmPasswordVisibilityToggle) {
                        Icon(
                            if (confirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                },
                visualTransformation = if (confirmPasswordVisible) {
                    androidx.compose.ui.text.input.VisualTransformation.None
                } else {
                    androidx.compose.ui.text.input.PasswordVisualTransformation()
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AmanotesColors.Secondary,
                    focusedLabelColor = AmanotesColors.Secondary,
                    focusedLeadingIconColor = AmanotesColors.Secondary
                )
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Error message
            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = AmanotesColors.Error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
            
            // Signup button
            PremiumButton(
                text = if (isLoading) "Enrolling..." else "Enroll in Academy",
                onClick = onSignup,
                enabled = !isLoading && 
                    email.isNotBlank() && 
                    password.isNotBlank() && 
                    confirmPassword.isNotBlank() && 
                    fullName.isNotBlank() && 
                    password == confirmPassword,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
