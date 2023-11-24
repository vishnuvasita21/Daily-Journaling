@Entity(tableName = "journal_entries")
data class JournalEntry(
    @PrimaryKey
    val entryId: String,
    val uid: String,
    val content: String,
    val timestamp: Long,
    val mood: String,
    val privacy: String,
    // Add other fields from the JSON
)
