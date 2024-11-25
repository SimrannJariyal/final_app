package com.example.uff.repositories


import com.example.uff.models.Task
import com.example.uff.network.RetrofitInstance
import retrofit2.Response

class TaskRepository {

    private val apiService = RetrofitInstance.apiService


    // Get tasks by user
    suspend fun getTasksByUser(userId: Int): Response<List<Task>> {
        return apiService.getTasksByUser(userId)
    }

    // Create task for a specific user
    suspend fun createTaskForUser(task: Task): Response<Task> {
        return apiService.createTaskForUser(task)
    }

    // Update task
    suspend fun updateTask(id: Int, task: Task): Response<Task> {
        return apiService.updateTask(id, task)
    }

    // Delete task
    suspend fun deleteTask(id: Int): Response<Void> {
        return apiService.deleteTask(id)
    }
}
