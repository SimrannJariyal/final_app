package com.example.uff

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.uff.pages.HomeScreen
import com.example.uff.pages.LoginScreen
import com.example.uff.pages.RegisterScreen
import com.example.uff.pages.SearchScreen
import com.example.uff.pages.NotificationScreen
import com.example.uff.pages.UnitScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "login"
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
            SearchScreen(navController = navController)
        }

        composable("notifications") {
            NotificationScreen(navController = navController)
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
