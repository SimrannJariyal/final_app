package com.example.uff.models
data class Task(
    val id: Int = 0, // Default value
    val user: Int = 0, // Default value, use actual user ID when available
    val title: String,
    val description: String,
    val isCompleted: Boolean = false, // Default to false
    val createdAt: String = "", // Default empty string, or use current timestamp if required
    val updatedAt: String = "" // Default empty string
)

