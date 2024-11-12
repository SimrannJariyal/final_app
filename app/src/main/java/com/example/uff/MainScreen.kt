package com.example.uff

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

import com.example.uff.pages.HomeScreen
import com.example.uff.pages.NotificationScreen
import com.example.uff.pages.SearchScreen
import com.example.uff.pages.UnitScreen
import com.example.uff.ui.theme.White // Correct import from ui.theme

@Composable
fun MainScreen(navController: NavController, modifier: Modifier = Modifier) {
    val navItemList = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("Notifications", Icons.Default.Notifications),
        NavItem("Search", Icons.Default.Search)
    )
    val selectedIndex = remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (selectedIndex.value == 0) {
                HomeTopAppBar()
            }
        },
        bottomBar = {
            NavigationBar {
                navItemList.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = selectedIndex.value == index,
                        onClick = {
                            selectedIndex.value = index
                        },
                        icon = { Icon(imageVector = navItem.icon, contentDescription = "Icon") },
                        label = { Text(text = navItem.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        ContentScreen(
            modifier = Modifier.padding(innerPadding),
            selectedIndex = selectedIndex.value,
            navController = navController
        )
    }
}

// ContentScreen - Dynamically displays content based on selected index
@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedIndex: Int, navController: NavController) {
    when (selectedIndex) {
        0 -> HomeScreen(navController = navController)
        1 -> NotificationScreen(navController = navController)
        2 -> SearchScreen()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar() {
    TopAppBar(
        title = {
            Text(
                text = "LearnHub",
                modifier = Modifier.fillMaxWidth(), // Ensure it fills width but doesn't take up all space
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = White // Ensure this white is visible on the background
                ),
                textAlign = TextAlign.Center // Center the title
            )
        },
        navigationIcon = {
            IconButton(onClick = { /* Handle menu icon click */ }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
            }
        },
        actions = {
            IconButton(onClick = { /* Handle search icon click */ }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            }
        },
        modifier = Modifier
            .padding(top = 2.dp) // Adjust padding if needed
            .clip(RoundedCornerShape(12.dp)), // Apply rounded corners
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF03A9F4) // Set the TopAppBar background color to blue
        )
    )
}
@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = "mainscreen"
    ) {
        composable("mainscreen") {
            MainScreen(navController = navController)
        }

        composable(
            "unitscreen/{subjectId}",
            arguments = listOf(navArgument("subjectId") { type = NavType.IntType })
        ) { backStackEntry ->
            val subjectId = backStackEntry.arguments?.getInt("subjectId") ?: 0
            UnitScreen(navController = navController, subjectId = subjectId)
        }
    }
}
