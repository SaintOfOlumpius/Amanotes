package com.example.amanotes.ui.compose.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(onBack: () -> Unit) {
    val categories = listOf(
        "All Notes",
        "Software Dev",
        "Weekly Meal Prep Plan",
        "Class Notes (Cybersecurity)",
        "Personal Journal / Reflection"
    )

    val mealPlan = listOf(
        "Breakfasts: Oats w/ banana & peanut butter, Boiled eggs w/ toast, Yogurt + granola + berries.",
        "Lunches: Grilled chicken + quinoa, Tuna pasta, Stir-fried beef w/ rice.",
        "Snacks: Mixed nuts, Protein bars, Apple slices with peanut butter.",
        "Reminder: Prep meals Sunday evening."
    )

    val appNotes = listOf(
        "Implement savings goals tracker",
        "Add Firebase Authentication",
        "UI update → cleaner dashboard with charts",
        "Debug crash on transaction history",
        "Write test cases for budget calculations",
        "Deadline: Friday, 6 Sept"
    )

    val classNotes = listOf(
        "Phishing → fake emails tricking users",
        "SQL Injection → manipulate queries",
        "DDoS → overwhelm server",
        "Man-in-the-Middle → intercept communications",
        "Key takeaway: validate input, enforce HTTPS, monitor traffic"
    )

    val journal = """Woke up at 4:30 AM today. Finished Security+ revision before breakfast.
Hit a new PR at gym (70kg bench). Feeling good but need more sleep.
Goal: 7 hours nightly, and spend more time with family."""

    Scaffold(topBar = { TopAppBar(title = { Text("Notes") }) }) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(16.dp)) {
            item { Text("Categories", style = MaterialTheme.typography.titleMedium) }
            items(categories.size) { i -> ListItem(headlineContent = { Text(categories[i]) }) }

            item { Spacer(Modifier.height(16.dp)) }
            item { Text("Weekly Meal Prep Plan", style = MaterialTheme.typography.titleMedium) }
            items(mealPlan.size) { i -> ListItem(headlineContent = { Text(mealPlan[i]) }) }

            item { Spacer(Modifier.height(16.dp)) }
            item { Text("App Notes (Cash Quest)", style = MaterialTheme.typography.titleMedium) }
            items(appNotes.size) { i -> ListItem(headlineContent = { Text(appNotes[i]) }) }

            item { Spacer(Modifier.height(16.dp)) }
            item { Text("Class Notes (Cybersecurity)", style = MaterialTheme.typography.titleMedium) }
            items(classNotes.size) { i -> ListItem(headlineContent = { Text(classNotes[i]) }) }

            item { Spacer(Modifier.height(16.dp)) }
            item { Text("Personal Journal", style = MaterialTheme.typography.titleMedium) }
            item { Text(journal) }
        }
    }
}


