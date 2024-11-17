package com.example.uff.pages

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import android.app.DownloadManager
import android.os.Environment
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.uff.models.Unit
import com.example.uff.network.RetrofitInstance
import kotlinx.coroutines.launch

@Composable
fun UnitScreen(
    navController: NavController,
    subjectId: Int
) {
    var units by remember { mutableStateOf<List<Unit>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    // Fetch the units associated with the subject ID
    LaunchedEffect(subjectId) {
        coroutineScope.launch {
            try {
                units = RetrofitInstance.apiService.getUnitsBySubject(subjectId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Back button to go back to the previous screen
        Button(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth()) {
            Text("Back")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // If there are no units, display a message
        if (units.isEmpty()) {
            Text("No units available.")
        } else {
            // Display units in a LazyColumn (scrollable list)
            LazyColumn {
                items(units) { unit ->
                    Column(modifier = Modifier.padding(8.dp)) {
                        // Display the unit name with the word "Unit" in bold
                        Text(text = "Unit: ", style = androidx.compose.ui.text.TextStyle(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold))
                        Text(text = unit.unit_name, modifier = Modifier.padding(bottom = 4.dp))

                        val context = LocalContext.current

                        // Row to display View PDF and Download PDF buttons on the same line
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            // Button to view the PDF
                            Button(onClick = { openPdfInBrowser(context, unit.pdf_file) }, modifier = Modifier.weight(1f)) {
                                Text("View PDF")
                            }

                            // Button to download the PDF
                            Button(onClick = { downloadPdf(context, unit.pdf_file) }, modifier = Modifier.weight(1f)) {
                                Text("Download PDF")
                            }
                        }
                    }
                }
            }
        }
    }
}

// Function to open the PDF in the browser
fun openPdfInBrowser(context: Context, pdfUrl: String?) {
    if (pdfUrl.isNullOrEmpty()) {
        Toast.makeText(context, "PDF URL is invalid.", Toast.LENGTH_SHORT).show()
        return
    }

    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(pdfUrl)).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }

    // Check if there is an app that can handle this intent
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        Toast.makeText(context, "No application found to open PDF.", Toast.LENGTH_SHORT).show()
    }
}

// Function to download the PDF using DownloadManager
fun downloadPdf(context: Context, pdfUrl: String?) {
    if (pdfUrl.isNullOrEmpty()) {
        Toast.makeText(context, "PDF URL is invalid.", Toast.LENGTH_SHORT).show()
        return
    }

    try {
        // Parse the URL
        val downloadUri = Uri.parse(pdfUrl)

        // Create the DownloadManager request
        val request = DownloadManager.Request(downloadUri).apply {
            setTitle("Downloading PDF")
            setDescription("Downloading PDF file...")
            // Save the PDF file in the public Downloads directory
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "downloaded_file.pdf")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        }

        // Get the system's DownloadManager service and enqueue the download
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadId = downloadManager.enqueue(request)

        // Log the download ID for debugging
        println("Download started with ID: $downloadId")

        // Show a toast indicating the download started
        Toast.makeText(context, "Download started...", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        // Handle any errors that might occur
        Toast.makeText(context, "Error starting download: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}