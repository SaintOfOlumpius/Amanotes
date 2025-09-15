package com.example.amanotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.amanotes.ui.compose.screens.AuthenticationScreen
import com.example.amanotes.ui.compose.screens.HomeScreen
import com.example.amanotes.ui.compose.screens.NotesScreen
import com.example.amanotes.ui.compose.screens.ProfileSettingsScreen
import com.example.amanotes.ui.compose.screens.ProjectDetailsScreen
import com.example.amanotes.ui.compose.theme.AmanotesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var darkMode by remember { mutableStateOf(true) }
            AmanotesTheme(darkTheme = darkMode) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppNav(darkMode = darkMode, onToggleDark = { darkMode = !darkMode })
                }
            }
        }
    }
}

@Composable
fun AppNav(darkMode: Boolean, onToggleDark: () -> Unit) {
    val navController = rememberNavController()
    val destinations = listOf("home", "project", "notes", "profile")
    Scaffold(bottomBar = {
        val current = navController.currentBackStackEntryAsState().value?.destination?.route
        if (current in destinations) {
            NavigationBar {
                NavigationBarItem(selected = current == "home", onClick = { navController.navigate("home") }, icon = { androidx.compose.material3.Icon(Icons.Default.Home, null) }, label = { Text("Home") })
                NavigationBarItem(selected = current == "project", onClick = { navController.navigate("project") }, icon = { androidx.compose.material3.Icon(Icons.AutoMirrored.Filled.List, null) }, label = { Text("Project") })
                NavigationBarItem(selected = current == "notes", onClick = { navController.navigate("notes") }, icon = { androidx.compose.material3.Icon(Icons.Default.Description, null) }, label = { Text("Notes") })
                NavigationBarItem(selected = current == "profile", onClick = { navController.navigate("profile") }, icon = { androidx.compose.material3.Icon(Icons.Default.Person, null) }, label = { Text("Profile") })
            }
        }
    }) { padding ->
        NavHost(navController = navController, startDestination = "auth", modifier = Modifier.padding(padding)) {
            composable("auth") { AuthenticationScreen(onAuthenticated = { navController.navigate("home") { popUpTo("auth") { inclusive = true } } }) }
            composable("home") { HomeScreen(onOpenProject = { navController.navigate("project") }, onOpenNotes = { navController.navigate("notes") }, onOpenProfile = { navController.navigate("profile") }) }
            composable("project") { ProjectDetailsScreen(onBack = { navController.popBackStack() }) }
            composable("profile") { ProfileSettingsScreen(darkMode = darkMode, onToggleDark = onToggleDark, onBack = { navController.popBackStack() }) }
            composable("notes") { NotesScreen(onBack = { navController.popBackStack() }) }
        }
    }
}


