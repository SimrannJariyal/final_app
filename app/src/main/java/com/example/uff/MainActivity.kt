package com.example.uff

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.uff.ui.theme.UffTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UffTheme {
                val navController = rememberNavController() // Remember the NavController
                AppNavHost(navController = navController) // Pass NavController to NavHost
            }
        }
    }
}
