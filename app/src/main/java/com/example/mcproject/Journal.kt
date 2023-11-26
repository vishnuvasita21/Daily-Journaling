package com.example.mcproject
data class Journal(
    val title: String = "",
    val content: String = "",
    val tags: List<String> = emptyList()
)
