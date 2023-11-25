package com.example.mcproject

// Inside JournalActivity.kt

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File


class JournalActivity : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private lateinit var adapter: JournalAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var exportViewModel: ExportViewModel

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

        exportViewModel = ViewModelProvider(this, ExportViewModelFactory())[ExportViewModel::class.java]
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
            R.id.export_as_pdf -> {
                val journalContent = "\n" +
                        "\"Before bed, I took some time to reflect on the day. I thought about the things that went well and the areas where I could improve. I also wrote down some goals for tomorrow, including getting up a little bit earlier and taking a few moments to meditate before starting my day.\\n\" +\n" +
                        "\"\\n\" +\n" +
                        "\"Overall, today was a good day. I feel grateful for the opportunities and experiences that came my way, and I’m looking forward to what tomorrow will bring."
                val journal = Journal("Title 1", journalContent, listOf("tag1", "tag2"))
                val pdfFile = exportViewModel.createPDF(journal)
                launchPDF(pdfFile)
                return true
            }
            // Add other menu item cases as needed
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun launchPDF(file: File) {
        val pdfUrl = FileProvider.getUriForFile(this, "${packageName}.provider", file)

        val intent = Intent(Intent.ACTION_VIEW)

        intent.setDataAndType(pdfUrl, "application/pdf")

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        try {
            startActivity(intent)
        } catch (e: Exception) {
            Log.e("ee", e.toString())
        }
    }
}
