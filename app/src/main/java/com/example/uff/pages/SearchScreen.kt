package com.example.uff.pages

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uff.NavItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController, modifier: Modifier = Modifier) {
    // Context for SharedPreferences
    val context = LocalContext.current
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    // Logout function to clear the access_token
    fun logout() {
        editor.remove("access_token")
        editor.apply()

        // Navigate to the login screen after logout
        navController.navigate("login") {
            popUpTo("login") { inclusive = true }
        }
    }

    // Define navigation drawer items
    val navItems = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("Profile", Icons.Default.Person),
        NavItem("To-do", Icons.Default.Create)
    )

    // Drawer state and coroutine scope
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Scaffold with ModalNavigationDrawer
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawerContent(
                navItems = navItems,
                onItemSelected = { label ->
                    when (label) {
                        "Home" -> navController.navigate("home")
                        "Profile" -> navController.navigate("profile")
                        "To-do" -> navController.navigate("todo")
                    }
                    scope.launch { drawerState.close() } // Close the drawer after navigation
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Search") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Open Drawer")
                        }
                    }
                )
            },
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Logout button
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { logout() }) {
                            Text("Logout", color = Color.Red)
                        }
                    }

                    // Main content
                    Text(
                        text = "Search Page",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        )
    }
}

@Composable
fun NavigationDrawerContent(navItems: List<NavItem>, onItemSelected: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "Navigation Drawer",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        navItems.forEach { item ->
            NavigationDrawerItem(
                label = { Text(item.label) },
                icon = { Icon(item.icon, contentDescription = null) },
                selected = false, // Handle selection state if needed
                onClick = { onItemSelected(item.label) },
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}
