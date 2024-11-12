package com.example.uff.pages

import android.content.Context
import android.content.Intent
import android.widget.Toast
import android.net.Uri
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
        Button(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth()) {
            Text("Back")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (units.isEmpty()) {
            Text("No units available.")
        } else {
            LazyColumn {
                items(units) { unit ->
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(unit.unit_name, modifier = Modifier.padding(bottom = 4.dp))

                        val context = LocalContext.current

                        Button(onClick = { openPdfInBrowser(context, unit.pdf_file) }, modifier = Modifier.fillMaxWidth()) {
                            Text("Download PDF")
                        }
                    }
                }
            }
        }
    }
}

fun openPdfInBrowser(context: Context, pdfUrl: String?) {
    if (pdfUrl.isNullOrEmpty()) {
        Toast.makeText(context, "PDF URL is invalid.", Toast.LENGTH_SHORT).show()
        return
    }

    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(pdfUrl)).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }

    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        Toast.makeText(context, "No application found to open PDF.", Toast.LENGTH_SHORT).show()
    }
}
