package com.example.mcproject

// Inside JournalActivity.kt

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class JournalActivity : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private lateinit var adapter: JournalAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal)

        searchView = findViewById(R.id.searchView)
        setUpSearchView()

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = JournalAdapter(mutableListOf())
        recyclerView.adapter = adapter
        dbHelper = DatabaseHelper(this)

        val db = dbHelper.writableDatabase
        db.delete(DatabaseHelper.TABLE_NAME, null, null)
        db.close()
        
        insertJournal(Journal("Title 1", "Content 1", listOf("tag1", "tag2")))
        insertJournal(Journal("Title 2", "Content 2", listOf("tag2", "tag3")))
    }

    private fun setUpSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                performSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    recyclerView.visibility = View.GONE
                } else {
                    recyclerView.visibility = View.VISIBLE
                    performSearch(newText)
                }

                return true
            }
        })
    }

    fun insertJournal(journal: Journal) {
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_TITLE, journal.title)
            put(DatabaseHelper.COLUMN_CONTENT, journal.content)
            put(DatabaseHelper.COLUMN_TAGS, journal.tags.joinToString(","))
        }

        val db = dbHelper.writableDatabase
        db.insert(DatabaseHelper.TABLE_NAME, null, values)
        db.close()
    }

    fun performSearch(query: String?) {
        val db = dbHelper.readableDatabase
        val selectionArgs = arrayOf("%$query%")
        val cursor = db.query(
            DatabaseHelper.TABLE_NAME,
            null,
            "${DatabaseHelper.COLUMN_TAGS} LIKE ?",
            selectionArgs,
            null,
            null,
            null
        )

        val journalList = ArrayList<Journal>()
        with(cursor) {
            while (moveToNext()) {
                val tagsString = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_TAGS))
                val tags = tagsString.split(",")
                if (query in tags) {
                    val title = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE))
                    val content = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_CONTENT))
                    val journal = Journal(title, content, tags)
                    journalList.add(journal)
                }
            }
        }
        cursor.close()
        adapter.updateList(journalList)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                // Handle Settings item click
                return true
            }
            // Add other menu item cases as needed
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
