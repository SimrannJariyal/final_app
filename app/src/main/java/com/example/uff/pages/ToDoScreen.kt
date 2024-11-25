package com.example.uff.pages

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.uff.viewmodels.TaskViewModel
import com.example.uff.models.Task

@Composable
fun ToDoScreen(navController: NavController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val taskViewModel: TaskViewModel = viewModel(factory = TaskViewModelFactory(sharedPreferences))
    val tasks by taskViewModel.tasks.observeAsState(emptyList())
    var isDialogOpen by remember { mutableStateOf(false) }
    var editingTask: Task? by remember { mutableStateOf(null) }

    // Open dialog for adding/updating task
    if (isDialogOpen) {
        TaskDialog(
            initialTitle = editingTask?.title ?: "",
            initialDescription = editingTask?.description ?: "",
            onDismiss = {
                isDialogOpen = false
                editingTask = null
            },
            onSave = { title, description ->
                if (title.isNotBlank() && description.isNotBlank()) {
                    editingTask?.let { task ->
                        taskViewModel.updateTask(task.id, task.copy(title = title, description = description))
                    } ?: run {
                        taskViewModel.addTask(Task(title = title, description = description))
                    }
                    isDialogOpen = false
                    editingTask = null
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "To-Do List",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (tasks.isEmpty()) {
            Text("No tasks available", style = MaterialTheme.typography.bodyLarge)
        } else {
            Column {
                tasks.forEach { task ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = task.title,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyLarge
                        )

                        IconButton(onClick = {
                            isDialogOpen = true
                            editingTask = task
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Task")
                        }

                        IconButton(onClick = { taskViewModel.deleteTask(task.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Task")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            isDialogOpen = true
            editingTask = null
        }) {
            Text("Add Task")
        }
    }
}

class TaskViewModelFactory(private val sharedPreferences: SharedPreferences) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(sharedPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
