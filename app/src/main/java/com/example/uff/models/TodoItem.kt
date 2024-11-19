package com.example.uff.models

data class TodoItem(
    val id: Int,
    val user: Int,
    val title: String,
    val description: String,
    val is_done: Boolean,
    val reminder: String?,
    val created_at: String,
    val updated_at: String
)

data class TaskStatus(
    val is_done: Boolean
)
