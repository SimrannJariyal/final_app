package com.example.uff

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.uff.pages.Contact
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
    val context = navController.context
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val isLoggedIn = sharedPreferences.getString("access_token", null) != null

    Scaffold { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn) "mainscreen" else "login",
            modifier = Modifier.padding(paddingValues)
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
                SearchScreen(navController = navController, paddingValues = paddingValues)
            }

            composable("notifications") {
                NotificationScreen(navController = navController)
            }

            composable("profile") {
                ProfileScreen(navController = navController)
            }

            composable("todo") {
                ToDoScreen(navController = navController)
            }

            composable("contact") {
                Contact()
            }

            composable(
                "unitscreen/{subId}",
                arguments = listOf(navArgument("subId") { type = NavType.IntType })
            ) { backStackEntry ->
                val subId = backStackEntry.arguments?.getInt("subId") ?: 0
                UnitScreen(navController = navController, subjectId = subId)
            }
        }
    }
}
