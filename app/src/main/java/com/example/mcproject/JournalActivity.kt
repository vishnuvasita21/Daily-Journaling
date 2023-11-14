package com.example.mcproject

// Inside JournalActivity.kt

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity

class JournalActivity : AppCompatActivity() {

    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal)

        searchView = findViewById(R.id.searchView)
        setUpSearchView()
    }

    private fun setUpSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                performSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                performSearch(newText)
                return true
            }
        })
    }

    private fun performSearch(query: String?) {
        // Implement your search logic here
        // You may want to filter your entries based on the query
        // Update your UI accordingly

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
