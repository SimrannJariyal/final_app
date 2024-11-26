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
import com.example.uff.network.ApiService
import com.example.uff.network.RetrofitInstance
import kotlinx.coroutines.launch

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File

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

    // Fetch user profile
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
    ) { uri: Uri? ->
        if (uri != null) {
            selectedPhotoUri = uri
            coroutineScope.launch {
                try {
                    isLoading = true
                    val response = UserRepository(RetrofitInstance.apiService).updateProfilePhoto(
                        userId, uri, context
                    )
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Photo updated successfully", Toast.LENGTH_SHORT).show()
                        val refreshedProfile = RetrofitInstance.apiService.getUserProfile(userId)
                        userProfile = refreshedProfile
                    } else {
                        Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Error uploading photo", Toast.LENGTH_SHORT).show()
                } finally {
                    isLoading = false
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Top Section with Background
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        val profilePhotoUrl = userProfile?.profile_photo_url
                        Image(
                            painter = rememberImagePainter(profilePhotoUrl ?: R.drawable.ic_launcher_background),
                            contentDescription = "Profile Background",
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .background(Color.LightGray),
                            contentScale = ContentScale.Crop
                        )

                        // Circular Image Over Background
                        Image(
                            painter = rememberImagePainter(profilePhotoUrl ?: R.drawable.ic_launcher_background),
                            contentDescription = "Profile Photo",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .align(Alignment.BottomEnd)
                                .padding(16.dp),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // User Info in Box
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFF0F0F0))
                            .padding(16.dp)
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.medium)
                    ) {
                        Column {
                            Text(
                                text = "Username: ${userProfile?.username}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Email: ${userProfile?.email}",
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Upload Photo Button
                    Button(
                        onClick = { imagePickerLauncher.launch("image/*") },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Upload Photo")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Bottom Buttons: Logout and Contact Us
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { handleLogout(navController) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text("Logout", color = Color.White)
                        }
                        Button(
                            onClick = { navController.navigate("contact") }
                        ) {
                            Text("Contact Us")
                        }
                    }
                }
            }
        }
    }
}

fun handleLogout(navController: NavController) {
    val context = navController.context
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().clear().apply()

    navController.navigate("login") {
        popUpTo("mainscreen") { inclusive = true }
    }
}


class UserRepository(private val apiService: ApiService) {

    suspend fun updateProfilePhoto(userId: Int, photoUri: Uri, context: Context): Response<Map<String, String>> {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(photoUri)
        val file = File(context.cacheDir, "profile_photo.jpg")

        inputStream.use { input ->
            file.outputStream().use { output ->
                input?.copyTo(output)
            }
        }

        val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val photoPart = MultipartBody.Part.createFormData("profile_photo", file.name, requestBody)

        return apiService.updateProfilePhoto(userId, photoPart)
    }
}
