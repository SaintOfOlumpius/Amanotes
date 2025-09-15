package com.example.amanotes.ui.compose.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun AuthenticationScreen(onAuthenticated: () -> Unit) {
    var isLogin by remember { mutableStateOf(true) }
    val offsetX = remember { Animatable(0f) }
    val thresholdPx = 140f
    val scope = rememberCoroutineScope()

    // Local form state so inputs are editable
    var loginUsername by remember { mutableStateOf("") }
    var loginPassword by remember { mutableStateOf("") }
    var signFirst by remember { mutableStateOf("") }
    var signLast by remember { mutableStateOf("") }
    var signUser by remember { mutableStateOf("") }
    var signPass by remember { mutableStateOf("") }
    var signPass2 by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Background hint labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Sign Up", style = MaterialTheme.typography.labelLarge)
            Text("Login", style = MaterialTheme.typography.labelLarge)
        }
        Spacer(Modifier.height(12.dp))

        // Draggable Card container
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp)
                .offset(x = offsetX.value.dp)
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        val newVal = offsetX.value + delta
                        scope.launch { offsetX.snapTo(newVal.coerceIn(-300f, 300f)) }
                    },
                    onDragStopped = {
                        // Decide target state by direction
                        val v = offsetX.value
                        if (v < -thresholdPx) {
                            isLogin = false
                        } else if (v > thresholdPx) {
                            isLogin = true
                        }
                        // snap back to center
                        scope.launch { offsetX.animateTo(0f, animationSpec = tween(250)) }
                    }
                ),
            ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(if (isLogin) "Login" else "Sign Up", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(16.dp))

                if (isLogin) {
                    OutlinedTextField(value = loginUsername, onValueChange = { loginUsername = it }, label = { Text("Username") }, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(value = loginPassword, onValueChange = { loginPassword = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(20.dp))
                    Button(onClick = onAuthenticated, modifier = Modifier.fillMaxWidth(), content = { Text("Login") })
                } else {
                    OutlinedTextField(value = signFirst, onValueChange = { signFirst = it }, label = { Text("First Name") }, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(value = signLast, onValueChange = { signLast = it }, label = { Text("Last Name") }, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(value = signUser, onValueChange = { signUser = it }, label = { Text("Username") }, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(value = signPass, onValueChange = { signPass = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(value = signPass2, onValueChange = { signPass2 = it }, label = { Text("Re-enter Password") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(20.dp))
                    Button(onClick = onAuthenticated, modifier = Modifier.fillMaxWidth(), content = { Text("Create account") })
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        Text("Drag the card left/right to switch", style = MaterialTheme.typography.labelMedium)
        Spacer(Modifier.height(8.dp))
        TextButton(onClick = onAuthenticated) { Text("Skip for now") }
    }
}


