package com.example.mcproject // Replace with your actual package name

import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.example.mcproject.Feature_Tag.AddTagDialogFragment

class JournalActivity : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: JournalAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var buttonAddTag: ImageButton
    private lateinit var searchView: SearchView

    // Activity Result API callback
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val updatedTag = result.data?.getStringExtra(TagDetailActivity.EXTRA_UPDATED_TAG)
            val deletedTag = result.data?.getStringExtra(TagDetailActivity.EXTRA_DELETED_TAG)

            updatedTag?.let {
                adapter.updateTag(it)
            }

            deletedTag?.let {
                adapter.removeTag(it)
            }

            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal)

        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)
        buttonAddTag = findViewById(R.id.buttonAddTag)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = JournalAdapter(mutableListOf()) { tagName ->
            val intent = Intent(this, TagDetailActivity::class.java).apply {
                putExtra(TagDetailActivity.EXTRA_TAG, tagName)
            }
            startForResult.launch(intent)
        }

        recyclerView.adapter = adapter
    dbHelper = DatabaseHelper(this)

        val db = dbHelper.writableDatabase
        db.delete(DatabaseHelper.TABLE_NAME, null, null)
        db.close()
        
        insertJournal(Journal("Title 1", "Content 1", listOf("tag1", "tag2")))
        insertJournal(Journal("Title 2", "Content 2", listOf("tag2", "tag3")))
        //db = FirebaseFirestore.getInstance()
        buttonAddTag.setOnClickListener {
            AddTagDialogFragment().apply {
                onTagCreated = { tagName ->
                    adapter.addTag(tagName)
                    adapter.notifyDataSetChanged()
                }
            }.show(supportFragmentManager, "AddTagDialog")
        }
    }

    private fun setUpSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter(newText)
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
