package com.example.uff.pages

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.uff.R
import com.example.uff.models.User
import com.example.uff.network.RetrofitInstance
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(navController: NavController) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var userProfile by remember { mutableStateOf<User?>(null) }
    var errorMessage by remember { mutableStateOf("") }
    var selectedPhotoUri by remember { mutableStateOf<Uri?>(null) }

    // Get user ID from SharedPreferences
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val userId = sharedPreferences.getInt("user_id", -1)

    // Fetch user profile
    LaunchedEffect(userId) {
        if (userId != -1) {
            try {
                val response = RetrofitInstance.apiService.getUserProfile(userId)
                userProfile = response
            } catch (e: Exception) {
                errorMessage = "Error fetching profile: ${e.localizedMessage}"
            }
        } else {
            errorMessage = "User ID not found. Please log in again."
        }
    }

    // Image picker for profile photo
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> selectedPhotoUri = uri }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        if (userProfile != null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                // Display profile photo
                Image(
                    painter = rememberImagePainter(
                        data = userProfile?.profilePhotoUrl ?: "",
                        builder = { placeholder(R.drawable.slider2) }
                    ),
                    contentDescription = "Profile Photo",
                    modifier = Modifier
                        .size(120.dp)
                        .padding(8.dp),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = userProfile?.username.orEmpty(),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                )
                Text(
                    text = userProfile?.email.orEmpty(),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.Gray,
                        fontSize = 16.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Upload photo button
                Button(onClick = { launcher.launch("image/*") }) {
                    Text("Upload Profile Photo")
                }

                // Save photo to backend
                selectedPhotoUri?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        scope.launch {
                            try {
                                val inputStream = context.contentResolver.openInputStream(it)
                                val filePart = createImageFilePart("profile_photo", inputStream)
                                RetrofitInstance.apiService.updateProfilePhoto(userId, filePart)
                                selectedPhotoUri = null // Clear selection
                            } catch (e: Exception) {
                                errorMessage = "Error uploading photo: ${e.localizedMessage}"
                            }
                        }
                    }) {
                        Text("Save Photo")
                    }
                }
            }
        } else if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        } else {
            CircularProgressIndicator()
        }
    }
}
