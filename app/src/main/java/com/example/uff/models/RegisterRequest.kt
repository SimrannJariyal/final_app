package com.example.uff.models

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)