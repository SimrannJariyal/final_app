package com.example.uff.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uff.models.Task
import com.example.uff.repositories.TaskRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class TaskViewModel(private val sharedPreferences: SharedPreferences) : ViewModel() {

    private val repository = TaskRepository()

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> get() = _tasks

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val userId: Int
        get() = sharedPreferences.getInt("user_id", -1) // Default to -1 if not logged in

    init {
        if (userId != -1) {
            fetchTasks()
        }
    }

    fun fetchTasks() {
        if (userId == -1) {
            _errorMessage.postValue("User not logged in.")
            return
        }

        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val response = repository.getTasksByUser(userId)
                handleResponse(response) { tasks -> _tasks.postValue(tasks) }
            } catch (e: Exception) {
                _errorMessage.postValue("Failed to fetch tasks: ${e.localizedMessage}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun addTask(task: Task) {
        if (userId == -1) {
            _errorMessage.postValue("User not logged in.")
            return
        }

        val taskWithUser = task.copy(user = userId)

        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val response = repository.createTaskForUser(taskWithUser)
                if (response.isSuccessful) fetchTasks()
                else _errorMessage.postValue("Failed to add task: ${response.errorBody()?.string()}")
            } catch (e: Exception) {
                _errorMessage.postValue("Failed to add task: ${e.localizedMessage}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun updateTask(id: Int, updatedFields: Task) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val response = repository.updateTask(id, updatedFields)
                if (response.isSuccessful) fetchTasks()
                else _errorMessage.postValue("Failed to update task: ${response.errorBody()?.string()}")
            } catch (e: Exception) {
                _errorMessage.postValue("Failed to update task: ${e.localizedMessage}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun deleteTask(id: Int) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val response = repository.deleteTask(id)
                if (response.isSuccessful) fetchTasks()
                else _errorMessage.postValue("Failed to delete task: ${response.errorBody()?.string()}")
            } catch (e: Exception) {
                _errorMessage.postValue("Failed to delete task: ${e.localizedMessage}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    private inline fun <T> handleResponse(
        response: Response<T>,
        onSuccess: (T) -> Unit
    ) {
        if (response.isSuccessful) {
            response.body()?.let { onSuccess(it) }
                ?: _errorMessage.postValue("Response body is null.")
        } else {
            _errorMessage.postValue("Error: ${response.errorBody()?.string()}")
        }
    }
}
