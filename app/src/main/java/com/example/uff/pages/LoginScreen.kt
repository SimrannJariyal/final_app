package com.example.uff.pages

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.uff.models.LoginRequest
import com.example.uff.network.RetrofitInstance
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController) {
    val scope = rememberCoroutineScope()
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                scope.launch {
                    try {
                        val response = RetrofitInstance.apiService.login(
                            LoginRequest(email = email.text, password = password.text)
                        )

                        // Store user_id in SharedPreferences
                        val sharedPreferences = navController.context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                        sharedPreferences.edit()
                            .putString("access_token", response.access_token)
                            .putInt("user_id", response.user_id)
                            .apply()

                        Toast.makeText(navController.context, "Login successful!", Toast.LENGTH_SHORT).show()
                        navController.navigate("mainscreen")
                    } catch (e: Exception) {
                        errorMessage = "Login failed: ${e.localizedMessage}"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }
    }
}
