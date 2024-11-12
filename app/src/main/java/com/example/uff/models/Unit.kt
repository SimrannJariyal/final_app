package com.example.uff.models

data class Unit(
    val id: Int,
    val subject: Int, // or String depending on your API response
    val unit_name: String,
    val pdf_file: String
)
