package com.example.uff

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.uff.pages.HomeScreen
import com.example.uff.pages.LoginScreen
import com.example.uff.pages.RegisterScreen
import com.example.uff.pages.SearchScreen
import com.example.uff.pages.NotificationScreen
import com.example.uff.pages.ProfileScreen
import com.example.uff.pages.ToDoScreen
import com.example.uff.pages.UnitScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    Scaffold(
        // Add the padding to the Scaffold to prevent overlap with the app bar and bottom navigation

    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "mainscreen",
            modifier = Modifier.padding(paddingValues) // Add padding to the NavHost content
        ) {
            composable("login") {
                LoginScreen(navController = navController)
            }

            composable("register") {
                RegisterScreen(navController = navController)
            }

            composable("mainscreen") {
                MainScreen(navController = navController)
            }

            composable("search") {
                // Pass navController and paddingValues to SearchScreen
                SearchScreen(navController = navController, paddingValues = paddingValues)
            }

            composable("notifications") {
                NotificationScreen(navController = navController)
            }
            composable("profile") {
                ProfileScreen()
            }
            composable("todo") {
                ToDoScreen()
            }

            // This is the route for the subject detail screen, passing subId as an argument
            composable(
                "unitscreen/{subId}",
                arguments = listOf(navArgument("subId") { type = NavType.IntType })
            ) { backStackEntry ->
                // Retrieve the subId argument from the navigation backStack
                val subId = backStackEntry.arguments?.getInt("subId") ?: 0
                UnitScreen(navController = navController, subjectId = subId)
            }
        }
    }
}
