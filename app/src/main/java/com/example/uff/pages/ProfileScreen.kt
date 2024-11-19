package com.example.uff.pages

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.uff.R
import com.example.uff.models.User
import com.example.uff.network.RetrofitInstance
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val userId = sharedPreferences.getInt("user_id", -1)

    if (userId == -1) {
        Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
        return
    }

    var userProfile by remember { mutableStateOf<User?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var selectedPhotoUri by remember { mutableStateOf<Uri?>(null) }

    val coroutineScope = rememberCoroutineScope()

    // Fetch user profile on screen load
    LaunchedEffect(userId) {
        coroutineScope.launch {
            try {
                val response = RetrofitInstance.apiService.getUserProfile(userId)
                userProfile = response
                isLoading = false
            } catch (e: Exception) {
                errorMessage = "Error fetching user profile"
                isLoading = false
                Log.e("ProfileScreen", "Error fetching user profile", e)
            }
        }
    }

    // Image picker
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> selectedPhotoUri = uri }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            errorMessage != null -> {
                Text(
                    text = errorMessage ?: "Unknown error",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            userProfile != null -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Display profile photo
                    val profilePhotoUrl = userProfile?.profile_photo_url
                    Image(
                        painter = rememberImagePainter(
                            data = profilePhotoUrl ?: R.drawable.ic_launcher_background,
                            builder = { crossfade(true) }
                        ),
                        contentDescription = "Profile Photo",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                            .padding(8.dp),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Display user information
                    userProfile?.let { profile ->
                        Text(
                            text = profile.username,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = profile.email,
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Upload button
                    Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                        Text(text = "Choose Photo")
                    }

                    // Save photo button
                    selectedPhotoUri?.let { uri ->
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = {
                            if (uri != Uri.EMPTY) {
                                coroutineScope.launch {
                                    try {
                                        val filePart = MultipartUtils.createImageFilePart(
                                            "profile_photo", uri, context
                                        )
                                        val updatedUser = RetrofitInstance.apiService.updateProfilePhoto(
                                            userId, filePart
                                        )

                                        userProfile = updatedUser
                                        selectedPhotoUri = null
                                        isLoading = false
                                        Toast.makeText(context, "Profile photo updated!", Toast.LENGTH_SHORT).show()
                                    } catch (e: Exception) {
                                        errorMessage = "Error uploading photo: ${e.localizedMessage}"
                                        Log.e("ProfileScreen", errorMessage, e)
                                        Toast.makeText(context, "Error uploading photo", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Toast.makeText(context, "No photo selected", Toast.LENGTH_SHORT).show()
                            }
                        }) {
                            Text(text = "Save Photo")
                        }
                    }
                }
            }
        }
    }
}
