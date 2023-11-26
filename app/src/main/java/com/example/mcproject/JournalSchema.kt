package com.example.mcproject

data class JournalSchema(
    val uid: String = "",
    val entryId: String = "",
    val title: String = "",
    val content: String = "",
    val timestamp: String = "",
    val photos: List<String> = emptyList(),
    val location: Location = Location(),
    val mood: String = "",
    val weather: Weather = Weather(),
    val privacy: String = "",
    val tags: List<String> = emptyList(),
    val wordCount: Int = 0
)

data class Location(
    val latitude: String = "",
    val longitude: String = "",
    val placeName: String = ""
)

data class Weather(
    val temperature: String = "",
    val condition: String = "",
    val windSpeed: String = ""
)
