package com.example.mcproject

import java.io.Serializable

data class Journal(
    val title: String = "",
    val content: String = "",
    val tags: List<String> = emptyList()
    ): Serializable
