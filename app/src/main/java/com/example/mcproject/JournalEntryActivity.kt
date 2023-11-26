package com.example.mcproject

import android.content.ContentValues
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class JournalEntryActivity : AppCompatActivity() {
    private lateinit var entryEditText: EditText
    private lateinit var rememberButton: Button
    private lateinit var titleText: EditText
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var wordCountTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal_entry)

        entryEditText = findViewById(R.id.textContent)
        rememberButton = findViewById(R.id.rememberButton)
        titleText = findViewById(R.id.textTitle)
        dbHelper = DatabaseHelper(this)
        wordCountTextView = findViewById(R.id.textView5)

        rememberButton.setOnClickListener {
            val titleContent: String = titleText.text.toString()
            val mainContent: String = entryEditText.text.toString()

            //Everyone can get your data and pass it here: by implementing the following steps
            //1. Update the Journal.kt class
            //2. Update the insertJournal method
            //3. Pass your value from here to the below insertJournal method.

            insertJournal(Journal(titleContent, mainContent, listOf("tag1", "tag2")))

            Toast.makeText(this, "Entry Stored successfully!", Toast.LENGTH_SHORT).show()

        }

        entryEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed for this example
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // Calculate the word count and update the textView5
                val wordCount = calculateWordCount(charSequence.toString())
                wordCountTextView.text = String.format("%d words", wordCount)
            }

            override fun afterTextChanged(editable: Editable?) {
                // Not needed for this example
            }
        })

        // If needed, you can clear the table in the database on each activity creation
        dbHelper.writableDatabase.delete(DatabaseHelper.TABLE_NAME, null, null)
    }

    private fun insertJournal(journal: Journal) {
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_TITLE, journal.title)
            put(DatabaseHelper.COLUMN_CONTENT, journal.content)
            put(DatabaseHelper.COLUMN_TAGS, journal.tags.joinToString(","))
        }

        val db = dbHelper.writableDatabase
        db.insert(DatabaseHelper.TABLE_NAME, null, values)
        db.close()
    }

    private fun calculateWordCount(text: String): Int {
        // Simple logic to count words (assuming words are separated by spaces)
        val words = text.split("\\s+".toRegex()).toTypedArray()
        return words.size
    }
}
