package com.example.uff

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Create
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

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawerContent(
                navItems = navItemList,
                onItemSelected = { label ->
                    selectedDrawerItem = label
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
//                            text = selectedDrawerItem,
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
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF03A9F4)
                    )
                )
            },
            bottomBar = {
                NavigationBar {
                    navItemList.forEachIndexed { index, navItem ->
                        NavigationBarItem(
                            selected = selectedDrawerItem == navItem.label,
                            onClick = { selectedDrawerItem = navItem.label },
                            icon = { Icon(navItem.icon, contentDescription = navItem.label) },
                            label = { Text(navItem.label) }
                        )
                    }
                }
            }
        ) { innerPadding ->
            ContentScreen(
                modifier = Modifier.padding(innerPadding),
                selectedItem = selectedDrawerItem,
                navController = navController
            )
        }
    }
}

@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedItem: String, navController: NavController) {
    when (selectedItem) {
        "Home" -> HomeScreen(navController = navController)
        "Search" -> SearchScreen(navController = navController)
        "To-Do" -> ToDoScreen()
        "Profile" -> ProfileScreen()
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
            style = MaterialTheme.typography.titleLarge,  // You can adjust the style as needed
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Tagline below the title
        Text(
            text = "Your Knowledge, Organized and Accessible!",
            style = MaterialTheme.typography.bodyMedium,  // Adjust the style if needed
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
    }
}
