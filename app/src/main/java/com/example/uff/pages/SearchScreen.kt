package com.example.uff.pages

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uff.ui.theme.White

@Composable
fun SearchScreen(navController: NavController, modifier: Modifier = Modifier) {
    // Get context for SharedPreferences
    val context = LocalContext.current
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    // Logout function to clear the access_token
    fun logout() {
        // Clear the access token from SharedPreferences
        editor.remove("access_token")
        editor.apply()

        // Navigate back to login screen after logout
        navController.navigate("login") {
            popUpTo("login") { inclusive = true } // Clear the navigation stack
        }
    }

    // Layout for the search screen
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFF0000)), // Background color (Red)
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logout button at the top
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = { logout() }) {
                Text("Logout", color = White)
            }
        }

        // Main content of the search screen
        Text(
            text = "Search Page",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
