package com.example.uff.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uff.models.Subject
import com.example.uff.network.RetrofitInstance
import kotlinx.coroutines.launch

@Composable
fun NotificationScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var subjects by remember { mutableStateOf<List<Subject>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    // Fetch subjects on launch
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                subjects = RetrofitInstance.apiService.getSubjects()
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle errors such as network issues here
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFCDDC39)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Notification Page",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (subjects.isNotEmpty()) {
            LazyColumn(
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                items(subjects) { subject ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // Navigate to UnitScreen with the selected subject ID
                                navController.navigate("unitscreen/${subject.id}")
                            }
                            .padding(8.dp)
                            .background(Color.White)
                    ) {
                        Text(
                            text = subject.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black,
                            modifier = Modifier.padding(8.dp)
                        )
                        Text(
                            text = subject.description,
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        } else {
            Text(
                text = "No subjects available.",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
