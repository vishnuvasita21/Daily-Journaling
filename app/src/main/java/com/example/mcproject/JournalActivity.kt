package com.example.mcproject

// Inside JournalActivity.kt

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
import com.google.firebase.firestore.FirebaseFirestore


class JournalActivity : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: JournalAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal)

        searchView = findViewById(R.id.searchView)
        setUpSearchView()

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = JournalAdapter(mutableListOf())
        recyclerView.adapter = adapter

        //db = FirebaseFirestore.getInstance()
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

    fun performSearch(query: String?) {
//        if (query != null) {
//            db.collection("journals")
//                .whereArrayContains("tags", query)
//                .get()
//                .addOnSuccessListener { documents ->
//                    val journalList = ArrayList<Journal>()
//                    for (document in documents) {
//                        val journal = document.toObject(Journal::class.java)
//                        journalList.add(journal)
//                    }
//                    // Update your RecyclerView adapter with the filtered list
//                    adapter.updateList(journalList)
//                }
//                .addOnFailureListener { exception ->
//                    Log.w(TAG, "Error getting documents: ", exception)
//                }
//        }

        val dummyJournalList = mutableListOf(
            Journal("Title 1", "Content 1", listOf("tag1", "tag2")),
            Journal("Title 2", "Content 2", listOf("tag2", "tag3")),
            // Add more dummy entries as needed
        )

        adapter.updateList(dummyJournalList)
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
