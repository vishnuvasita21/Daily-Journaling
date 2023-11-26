package com.example.mcproject

import java.io.Serializable
import java.util.Date

data class Journal(
    val title: String = "",
    val content: String = "",
    val tags: List<String> = emptyList(),
    val date: String = "",
    ): Serializable
