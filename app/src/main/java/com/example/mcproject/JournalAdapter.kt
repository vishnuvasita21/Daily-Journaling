package com.example.mcproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class JournalAdapter(
    private val allTags: MutableList<String>, // Keep a full list of tags
    private val onTagClicked: (String) -> Unit
) : RecyclerView.Adapter<JournalAdapter.ViewHolder>() {

    // This list will hold the currently displayed tags, which may be filtered
    private var displayedTags: MutableList<String> = allTags.toMutableList()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tagTextView: TextView = itemView.findViewById(R.id.tagsTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tag_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tag = displayedTags[position]
        holder.tagTextView.text = tag
        holder.itemView.setOnClickListener { onTagClicked(tag) }
    }

    override fun getItemCount(): Int = displayedTags.size

    fun addTag(tag: String) {
        if (!allTags.contains(tag)) {
            allTags.add(tag)
            displayedTags.add(tag)
            notifyItemInserted(displayedTags.size - 1)
        }
    }

    fun updateTag(updatedTag: String) {
        val indexAll = allTags.indexOfFirst { it == updatedTag }
        val indexDisplayed = displayedTags.indexOfFirst { it == updatedTag }

        if (indexAll != -1) {
            allTags[indexAll] = updatedTag
        }
        if (indexDisplayed != -1) {
            displayedTags[indexDisplayed] = updatedTag
            notifyItemChanged(indexDisplayed)
        }
    }

    fun removeTag(deletedTag: String) {
        val indexAll = allTags.indexOf(deletedTag)
        val indexDisplayed = displayedTags.indexOf(deletedTag)

        if (indexAll != -1) {
            allTags.removeAt(indexAll)
        }
        if (indexDisplayed != -1) {
            displayedTags.removeAt(indexDisplayed)
            notifyItemRemoved(indexDisplayed)
        }
    }

    fun filter(query: String?) {
        if (query.isNullOrEmpty()) {
            displayedTags = allTags.toMutableList()
        } else {
            displayedTags = allTags.filter { it.contains(query, ignoreCase = true) }.toMutableList()
        }
        notifyDataSetChanged()
    }
}
