package com.example.mcproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Button
import android.view.View
import android.content.Intent

class ViewJournal : AppCompatActivity() {
    // define variables for the elements in activity_view_journal
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_journal)

        // accept data from JournalFragment and accept the Journal object.

        val journal = intent.getSerializableExtra("journal") as Journal

        // get objects for entities in activity_view_journal

        val textTitle = findViewById<TextView>(R.id.textTitle)
        val textContent = findViewById<TextView>(R.id.textContent)
        val backButton = findViewById<Button>(R.id.backButton)

        // assign text title to textTitle entity from the layout
        textTitle.text = journal.title

        // assign text content to textContent entity from layout.
        textContent.text = journal.content

        // setup back button to go to previous activity in stack
        backButton.setOnClickListener {
            finish()
        }
    }
}