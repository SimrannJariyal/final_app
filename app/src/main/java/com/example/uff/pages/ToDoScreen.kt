package com.example.uff.pages

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

    // Column for the content of ToDo screen with scrollable content
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 49.dp)// Add padding for scrollable content
    ) {
        // "Add Task" Button placed at the top
        item {
            Button(
                onClick = {
                    isDialogOpen = true
                    editingTask = null
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)

            ) {
                Text("Add Task")
            }
        }

        // Title for the To-Do list
        item {
            Text(
                text = "To-Do List",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // If no tasks are available, show this message
        item {
            if (tasks.isEmpty()) {
                Text("No tasks available", style = MaterialTheme.typography.bodyLarge)
            }
        }

        // Display task cards
        items(tasks) { task ->
            TaskCard(
                task = task,
                onEdit = {
                    isDialogOpen = true
                    editingTask = task
                },
                onDelete = { taskViewModel.deleteTask(task.id) }
            )
        }
    }
}


@Composable
fun TaskCard(task: Task, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = task.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Task")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Task")
                }
            }
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
