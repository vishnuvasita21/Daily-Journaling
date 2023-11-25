package com.example.mcproject // Replace with your actual package name

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mcproject.Feature_Tag.AddTagDialogFragment

class JournalActivity : AppCompatActivity() {

    private lateinit var adapter: JournalAdapter
    private lateinit var recyclerView: RecyclerView
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

        buttonAddTag.setOnClickListener {
            AddTagDialogFragment().apply {
                onTagCreated = { tagName ->
                    adapter.addTag(tagName)
                    adapter.notifyDataSetChanged()
                }
            }.show(supportFragmentManager, "AddTagDialog")
        }

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
}
