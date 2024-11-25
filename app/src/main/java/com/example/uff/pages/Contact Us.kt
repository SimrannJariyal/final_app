package com.example.uff.pages
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle

@Composable
fun Contact() {
    val context = LocalContext.current

    // State variables for the input fields
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isFormSubmitted by remember { mutableStateOf(false) } // Flag to show Toast message after sending

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top, // Arrange content starting from the top
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Contact Us Title at the top with a black color
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp) // Added padding for spacing at the top
                .background(Color.White, shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Contact Us",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                color = Color.Black, // Changed to black
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp)) // Add spacing below the title

        // Name field
        Text(text = "Name", modifier = Modifier.align(Alignment.Start), fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
            singleLine = true,
            placeholder = { Text(text = "Enter your name") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Email field
        Text(text = "Email", modifier = Modifier.align(Alignment.Start), fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            placeholder = { Text(text = "Enter your email") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Message field
        Text(text = "Message", modifier = Modifier.align(Alignment.Start), fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = message,
            onValueChange = { message = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(vertical = 8.dp)
                .border(2.dp, Color.Gray, RoundedCornerShape(8.dp)),
            placeholder = { Text(text = "Enter your message") },
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Send Button
        Button(
            onClick = {
                if (email.isNotEmpty() && message.isNotEmpty()) {
                    sendEmail(context, name, email, message)
                    isFormSubmitted = true  // Set form as submitted
                } else {
                    Toast.makeText(context, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.5f),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Text(text = "Send", color = Color.White)
        }

        // Show Toast message when form is submitted
        if (isFormSubmitted) {
            Toast.makeText(context, "Thanks for your message!", Toast.LENGTH_SHORT).show()
            isFormSubmitted = false  // Reset after showing toast
        }
    }
}

// Function to handle sending email via Intent
fun sendEmail(context: android.content.Context, name: String, email: String, message: String) {
    val recipient = "guptakshitij266@gmail.com" // Replace with your email address
    val subject = "Contact Form: $name"
    val body = """
        Name: $name
        Email: $email
        
        Message:
        $message
    """.trimIndent()

    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:") // Only email apps should handle this
        putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
    }

    try {
        context.startActivity(Intent.createChooser(intent, "Send Email"))
    } catch (e: Exception) {
        Toast.makeText(context, "No email app found", Toast.LENGTH_SHORT).show()
    }
}
