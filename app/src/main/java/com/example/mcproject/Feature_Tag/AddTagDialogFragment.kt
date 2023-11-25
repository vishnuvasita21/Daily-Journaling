package com.example.mcproject.Feature_Tag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.mcproject.Journal
import com.example.mcproject.R

class AddTagDialogFragment : DialogFragment() {
    var onTagCreated: ((String) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_add_tag, container, false)

        val editTextTitle = view.findViewById<EditText>(R.id.editTextTitle)
        val buttonSubmit = view.findViewById<Button>(R.id.buttonSubmit)

        buttonSubmit.setOnClickListener {
            val title = editTextTitle.text.toString()

            if (title.isNotEmpty()) {
                onTagCreated?.invoke(title)
                dismiss()
            } else {
                editTextTitle.error = "Title cannot be empty"
            }
        }

        return view
    }
}
