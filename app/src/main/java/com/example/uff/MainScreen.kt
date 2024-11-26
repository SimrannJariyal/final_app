package com.example.uff

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
    val navDrawerItemList = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("Search", Icons.Default.Search),
        NavItem("To-Do", Icons.Default.Create),
        NavItem("Profile", Icons.Default.AccountCircle),
        NavItem("Contact Us", Icons.Default.Email), // Keep "Contact Us" only in the drawer
    )
    val bottomNavItemList = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("Search", Icons.Default.Search),
        NavItem("To-Do", Icons.Default.Create),
        NavItem("Profile", Icons.Default.AccountCircle),
    ) // Exclude "Contact Us" here

    val sharedPreferences = LocalContext.current.getSharedPreferences("your_preferences", Context.MODE_PRIVATE)
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedDrawerItem by remember { mutableStateOf("Home") }
    var selectedBottomNavItem by remember { mutableStateOf("Home") }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawerContent(
                navItems = navDrawerItemList,
                onItemSelected = { label ->
                    when (label) {
                        "Logout" -> handleLogout(navController)
                        "Contact Us" -> {
                            navController.navigate("contact")
                            scope.launch { drawerState.close() }
                        }
                        else -> {
                            selectedDrawerItem = label
                            selectedBottomNavItem = label
                            scope.launch { drawerState.close() }
                        }
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
                    bottomNavItemList.forEach { navItem ->
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
            ContentScreen(
                modifier = Modifier.padding(innerPadding),
                selectedItem = selectedBottomNavItem,
                navController = navController,
                paddingValues = innerPadding,
                sharedPreferences = sharedPreferences
            )
        }
    }
}


@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedItem: String, navController: NavController, paddingValues: PaddingValues, sharedPreferences: SharedPreferences) {
    when (selectedItem) {
        "Home" -> HomeScreen(navController = navController)
        "Search" -> SearchScreen(navController = navController, paddingValues = paddingValues)
        "To-Do" -> ToDoScreen(navController=navController)
        "Profile" -> ProfileScreen(navController = navController)
    }
}

fun handleLogout(navController: NavController) {
    val sharedPreferences = navController.context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().clear().apply()

    navController.navigate("login") {
        popUpTo("mainscreen") { inclusive = true }
    }
}

@Composable
fun NavigationDrawerContent(navItems: List<NavItem>, onItemSelected: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.7f) // Control the drawer width
            .fillMaxHeight()
            .background(Color.White) // Set background color to white
            .padding(16.dp)
    ) {
        Text(
            text = "LearnHub",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Text(
            text = "Your Knowledge, Organized and Accessible!",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        navItems.forEach { item ->
            NavigationDrawerItem(
                label = { Text(item.label) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                selected = false,
                onClick = { onItemSelected(item.label) },
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        NavigationDrawerItem(
            label = { Text("Logout") },
            icon = { Icon(Icons.Default.ExitToApp, contentDescription = "Logout") },
            selected = false,
            onClick = { onItemSelected("Logout") },
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}
