package com.example.uff.models
data class Task(
    val id: Int = 0,
    val user: Int = 0,
    val title: String = "",
    val description: String = "",
    val is_completed: Boolean = false,
    val created_at: String = "",
    val updated_at: String = ""
)
