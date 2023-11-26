package com.example.mcproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

interface OnItemClickListener {
    fun onItemClick(journal: Journal)
}

class JournalAdapter(var journalList: MutableList<Journal>, private val clickListener: OnItemClickListener) :
    RecyclerView.Adapter<JournalAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_journal, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val journal = journalList[position]
        holder.titleTextView.text = journal.title
        holder.contentTextView.text = journal.content

        holder.itemView.setOnClickListener {
            clickListener.onItemClick(journal)
        }
    }

    override fun getItemCount(): Int {
        return journalList.size
    }

    fun updateList(newList: List<Journal>) {
        // Update the existing list with new data
        // You may need to implement a proper diffing mechanism for efficiency
        journalList.clear()
        journalList.addAll(newList)
        notifyDataSetChanged()
    }
}
