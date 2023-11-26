package com.example.mcproject

data class Journal(
    val id: Int,
    val title: String,
    val content: List<String>,
    val tags: MutableList<String>,
    val notes: MutableMap<String, String> = mutableMapOf(), // Maps tag names to content
    val privacy: String = "Private", // Default to private
)