package com.example.mcproject

data class JournalEntry(
    val uid: String,
    val entryId: String,
    val content: String,
    val timestamp: String,
    val photos: ArrayList<String>,
    val location: Location,
    val mood: String,
    val weather: Weather,
    val privacy: String,
    val tags: ArrayList<String>,
    val wordCount: Int
)

data class Location(
    val latitude: String,
    val longitude: String,
    val placeName: String
)

data class Weather(
    val temperature: String,
    val condition: String,
    val windSpeed: String
)

fun main() {
    // Creating an ArrayList of Entry objects
    val entryList = ArrayList<JournalEntry>()

    // Example entry
    val entry1 = JournalEntry(
        "user_id",
        "entry_id",
        "text_entry_content",
        "entry_creation_timestamp",
        arrayListOf("photo_url_1", "photo_url_2"),
        Location("latitude_value", "longitude_value", "place_name_value"),
        ":)",
        Weather("temperature_value", "weather_condition", "wind_speed_value"),
        "private/public/shared",
        arrayListOf("tag1", "tag2"),
        250
    )

    // Adding the entry to the list
    entryList.add(entry1)

    // You can add more entries as needed
}