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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.amanotes.R
import com.example.amanotes.utils.LocaleManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.amanotes.di.ServiceLocator
import com.example.amanotes.ui.compose.components.LoadingScreen
import com.example.amanotes.ui.compose.screens.AuthScreen
import com.example.amanotes.ui.compose.screens.HomeScreen
import com.example.amanotes.ui.compose.screens.NotesScreen
import com.example.amanotes.ui.compose.screens.ProfileSettingsScreen
import com.example.amanotes.ui.compose.screens.ProjectDetailsScreen
import com.example.amanotes.ui.compose.theme.AmanotesTheme
import com.example.amanotes.ui.compose.utils.LocaleAwareContent
import com.example.amanotes.ui.main.MainViewModel

class MainActivity : ComponentActivity() {
    override fun attachBaseContext(newBase: android.content.Context) {
        // Apply locale before super.attachBaseContext to ensure it's set correctly
        // This is called before onCreate, so we need to use runBlocking to read from DataStore
        val languageCode = LocaleManager.getSavedLanguageCode(newBase)
        val language = LocaleManager.Language.fromCode(languageCode)
        val context = LocaleManager.setLocale(newBase, language)
        super.attachBaseContext(context)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Ensure locale is applied on activity creation as well
        // This ensures the locale is set for Compose UI
        val languageCode = LocaleManager.getSavedLanguageCode(this)
        val language = LocaleManager.Language.fromCode(languageCode)
        LocaleManager.updateAppLocale(this, language)
        // Also update the configuration
        LocaleManager.updateConfiguration(this, language)
        
        setContent {
            val context = LocalContext.current
            
            // Initialize dependencies
            val authRepository = remember { ServiceLocator.provideAuthRepository(context) }
            val userPreferences = remember { ServiceLocator.provideUserPreferences(context) }
            val mainViewModel = remember { MainViewModel(authRepository, userPreferences) }
            
            // Collect states - including language for reactive updates
            val darkMode by mainViewModel.darkMode.collectAsStateWithLifecycle(initialValue = true)
            val appTheme by mainViewModel.appTheme.collectAsStateWithLifecycle()
            val isAuthenticated by mainViewModel.isAuthenticated.collectAsStateWithLifecycle()
            val languageCode by userPreferences.language.collectAsStateWithLifecycle(initialValue = "en")
            
            // Wrap the entire app in locale-aware context
            // When languageCode changes, the context is recreated and UI recomposes
            LocaleAwareContent(languageCode = languageCode) {
                AmanotesTheme(appTheme = appTheme, darkTheme = darkMode) {
                    Surface(color = MaterialTheme.colorScheme.background) {
                        when (isAuthenticated) {
                            null -> LoadingScreen(stringResource(R.string.checking_authentication))
                            else -> AppNav(
                                darkMode = darkMode,
                                appTheme = appTheme,
                                onToggleDark = mainViewModel::toggleDarkMode,
                                onThemeChange = mainViewModel::updateAppTheme,
                                isAuthenticated = isAuthenticated,
                                onAuthSuccess = mainViewModel::onAuthenticationSuccess,
                                onLogout = mainViewModel::onLogout
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AppNav(
    darkMode: Boolean,
    appTheme: com.example.amanotes.ui.compose.theme.AppTheme,
    onToggleDark: () -> Unit,
    onThemeChange: (com.example.amanotes.ui.compose.theme.AppTheme) -> Unit,
    isAuthenticated: Boolean?,
    onAuthSuccess: () -> Unit,
    onLogout: () -> Unit
) {
    val navController = rememberNavController()
    val destinations = listOf("home", "project", "notes", "profile")
    
    // Handle authentication state changes
    LaunchedEffect(isAuthenticated) {
        when (isAuthenticated) {
            true -> {
                // User is authenticated, navigate to home if currently on auth
                val currentRoute = navController.currentBackStackEntry?.destination?.route
                if (currentRoute == "auth") {
                    navController.navigate("home") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            }
            false -> {
                // User is not authenticated, navigate to auth
                navController.navigate("auth") {
                    popUpTo(0) { inclusive = true }
                }
            }
            null -> {
                // Still checking authentication status, do nothing
            }
        }
    }
    Scaffold(bottomBar = {
        val current = navController.currentBackStackEntryAsState().value?.destination?.route
        if (current in destinations) {
            NavigationBar {
                NavigationBarItem(selected = current == "home", onClick = { navController.navigate("home") }, icon = { androidx.compose.material3.Icon(Icons.Default.Home, null) }, label = { Text(stringResource(R.string.nav_home)) })
                NavigationBarItem(selected = current == "project", onClick = { navController.navigate("project") }, icon = { androidx.compose.material3.Icon(Icons.AutoMirrored.Filled.List, null) }, label = { Text(stringResource(R.string.nav_project)) })
                NavigationBarItem(selected = current == "notes", onClick = { navController.navigate("notes") }, icon = { androidx.compose.material3.Icon(Icons.Default.Description, null) }, label = { Text(stringResource(R.string.nav_notes)) })
                NavigationBarItem(selected = current == "profile", onClick = { navController.navigate("profile") }, icon = { androidx.compose.material3.Icon(Icons.Default.Person, null) }, label = { Text(stringResource(R.string.nav_profile)) })
            }
        }
    }) { padding ->
        NavHost(
            navController = navController, 
            startDestination = if (isAuthenticated == true) "home" else "auth", 
            modifier = Modifier.padding(padding)
        ) {
            composable("auth") { 
                AuthScreen(onAuthSuccess = onAuthSuccess) 
            }
            composable("home") { HomeScreen(onOpenProject = { navController.navigate("project") }, onOpenNotes = { navController.navigate("notes") }, onOpenProfile = { navController.navigate("profile") }) }
            composable("project") { ProjectDetailsScreen(onBack = { navController.popBackStack() }) }
                   composable("profile") {
                       ProfileSettingsScreen(
                           darkMode = darkMode,
                           appTheme = appTheme,
                           onToggleDark = onToggleDark,
                           onThemeChange = onThemeChange,
                           onBack = { navController.popBackStack() },
                           onNavigateToAuth = onLogout
                       )
                   }
            composable("notes") { NotesScreen(onBack = { navController.popBackStack() }) }
        }
    }
}


