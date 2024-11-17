package com.example.uff

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uff.pages.HomeScreen
import com.example.uff.pages.ProfileScreen
import com.example.uff.pages.SearchScreen
import com.example.uff.pages.ToDoScreen
import com.example.uff.ui.theme.White
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, modifier: Modifier = Modifier) {
    val navItemList = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("Search", Icons.Default.Search),
        NavItem("To-Do", Icons.Default.Create),
        NavItem("Profile", Icons.Default.AccountCircle)
    )

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedDrawerItem by remember { mutableStateOf("Home") }
    var selectedBottomNavItem by remember { mutableStateOf("Home") }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawerContent(
                navItems = navItemList,
                onItemSelected = { label ->
                    if (label == "Logout") {
                        handleLogout(navController)
                    } else {
                        selectedDrawerItem = label
                        selectedBottomNavItem = label
                        scope.launch { drawerState.close() }
                    }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "LearnHub",
                            modifier = Modifier.fillMaxWidth(),
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = White
                            ),
                            textAlign = TextAlign.Center
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        IconButton(onClick = { selectedBottomNavItem = "Search" }) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF03A9F4)
                    )
                )
            },
            bottomBar = {
                NavigationBar {
                    navItemList.forEach { navItem ->
                        NavigationBarItem(
                            selected = selectedBottomNavItem == navItem.label,
                            onClick = {
                                selectedBottomNavItem = navItem.label
                            },
                            icon = { Icon(navItem.icon, contentDescription = navItem.label) },
                            label = { Text(navItem.label) }
                        )
                    }
                }
            }
        ) { innerPadding ->
            // Passing paddingValues into ContentScreen
            ContentScreen(
                modifier = Modifier.padding(innerPadding),
                selectedItem = selectedBottomNavItem,
                navController = navController,
                paddingValues = innerPadding
            )
        }
    }
}

@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedItem: String, navController: NavController, paddingValues: PaddingValues) {
    when (selectedItem) {
        "Home" -> HomeScreen(navController = navController)
        "Search" -> SearchScreen(navController = navController, paddingValues = paddingValues)
        "To-Do" -> ToDoScreen()
        "Profile" -> ProfileScreen()
    }
}

fun handleLogout(navController: NavController) {
    // Perform the necessary logout actions such as clearing session, tokens, etc.
    // Example: Reset any shared preferences or clear session data

    // After performing logout actions, navigate to the login screen
    navController.navigate("login") {
        popUpTo("login") { inclusive = true }  // This will clear the back stack
    }
}

@Composable
fun NavigationDrawerContent(navItems: List<NavItem>, onItemSelected: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .fillMaxHeight()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // LearnHub title at the top
        Text(
            text = "LearnHub",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Tagline below the title
        Text(
            text = "Your Knowledge, Organized and Accessible!",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Spacer for separation
        Spacer(modifier = Modifier.height(16.dp))

        // Navigation items
        navItems.forEach { item ->
            NavigationDrawerItem(
                label = { Text(item.label) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                selected = false,
                onClick = { onItemSelected(item.label) },
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        // Logout item at the bottom of the drawer
        NavigationDrawerItem(
            label = { Text("Logout") },
            icon = { Icon(Icons.Default.ExitToApp, contentDescription = "Logout") },
            selected = false,
            onClick = { onItemSelected("Logout") },
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}
