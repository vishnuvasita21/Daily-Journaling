package com.example.mcproject

// JournalFragment.kt

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class JournalFragment : Fragment(), OnItemClickListener {

    private lateinit var searchView: SearchView
    private lateinit var adapter: JournalAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_journal, container, false)

        searchView = view.findViewById(R.id.searchView)
        setUpSearchView()

        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = JournalAdapter(mutableListOf(), this)
        recyclerView.adapter = adapter

        dbHelper = DatabaseHelper(requireContext())

        insertJournal(Journal("Title 1", "Content 1", listOf("tag1", "tag2")))
        insertJournal(Journal("Title 2", "Content 2", listOf("tag2", "tag3")))

        return view
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

    override fun onItemClick(journal: Journal) {
        val intent = Intent(requireContext(), ViewJournal::class.java)
        intent.putExtra("journal", journal)
        startActivity(intent)
    }
}