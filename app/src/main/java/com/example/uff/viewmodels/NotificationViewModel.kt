package com.example.uff.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uff.models.Subject
import com.example.uff.network.RetrofitInstance
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

class NotificationViewModel : ViewModel() {

    private val _subjects = mutableStateOf<List<Subject>>(emptyList())
    val subjects: State<List<Subject>> = _subjects

    init {
        getSubjects()
    }

    private fun getSubjects() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.apiService.getSubjects()
                _subjects.value = response
            } catch (e: Exception) {
                // Handle errors here
                println("Error fetching subjects: ${e.localizedMessage}")
            }
        }
    }
}
