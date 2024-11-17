package com.example.uff.pages

import android.widget.Toast
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uff.network.RetrofitInstance
import com.example.uff.models.Subject
import com.example.uff.network.RetrofitInstance.apiService
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController, paddingValues: PaddingValues) {
    var query by remember { mutableStateOf("") }
    var subjects by remember { mutableStateOf<List<Subject>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current // Get the context for Toast

    // Launch search logic when query changes or screen is recomposed
    LaunchedEffect(query) {
        isLoading = true

        try {
            // Fetch subjects from the API
            val subjectsResponse = apiService.getSubjects()

            // Print the raw response for debugging
            Log.d("APIResponse", "Subjects: $subjectsResponse")

            // Sort the subjects alphabetically
            subjects = subjectsResponse.sortedBy { it.name }

            // Filter the subjects based on the search query
            if (query.isNotEmpty()) {
                subjects = subjects.filter { it.name.contains(query, ignoreCase = true) }
            }

            isLoading = false
        } catch (e: Exception) {
            // Show error toast on failure in a composable context
            Toast.makeText(context, "Error fetching data", Toast.LENGTH_SHORT).show()
            isLoading = false
        }
    }

    // Function to generate a random color
    fun getRandomColor(): Color {
        return Color(
            Random.nextFloat(),
            Random.nextFloat(),
            Random.nextFloat()
        )
    }

    // Layout for the search screen
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .padding(paddingValues)
    ) {
        // Search box with icon and placeholder text
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp) // Make search bar bigger
                .background(Color(0xFFB3E5FC)) // Light blue background color
                .padding(horizontal = 16.dp)
        ) {
            Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Gray)

            BasicTextField(
                value = query,
                onValueChange = { value -> query = value },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp, color = Color.Black)
            )

            // Display "Search here" as placeholder when query is empty
            if (query.isEmpty()) {
                Text("Search here", style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp, color = Color.Gray))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Show loading indicator if searching
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        // Show search results (subjects)
        if (subjects.isNotEmpty()) {
            Text("Recommendations:", style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp))
            Spacer(modifier = Modifier.height(16.dp)) // Increased space between heading and results

            subjects.forEach { subject ->
                // Display each subject in a rounded box with a random background color
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp) // Increased space between subjects
                        .background(getRandomColor(), shape = MaterialTheme.shapes.medium)
                        .padding(18.dp)
                ) {
                    Text(
                        subject.name,
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp, color = Color.White)
                    )
                }
            }
        }
    }
}
