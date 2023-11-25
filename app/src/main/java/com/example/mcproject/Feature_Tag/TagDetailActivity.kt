package com.example.mcproject

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class TagDetailActivity : AppCompatActivity() {

    private lateinit var editTextTagName: EditText
    private lateinit var editTextContent: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag_detail)

        // Retrieve the passed tag name and content (if provided)
        val tagName = intent.getStringExtra(EXTRA_TAG) ?: ""

        // Initialize views
        editTextTagName = findViewById(R.id.editTextTagName)
        editTextContent = findViewById(R.id.editTextContent)
        val buttonSave = findViewById<Button>(R.id.buttonSave)
        val buttonDelete = findViewById<Button>(R.id.buttonDelete)
        val buttonUpdate = findViewById<Button>(R.id.buttonUpdate)

        // Set the retrieved tag name and content (if provided) to the EditText fields
        editTextTagName.setText(tagName)
        // Assume that EXTRA_CONTENT is used for passing content; otherwise remove this line
        editTextContent.setText(intent.getStringExtra(EXTRA_CONTENT))

        buttonSave.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_UPDATED_TAG, editTextTagName.text.toString())
            resultIntent.putExtra(EXTRA_UPDATED_CONTENT, editTextContent.text.toString())
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        buttonDelete.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_DELETED_TAG, tagName)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        buttonUpdate.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_UPDATED_TAG, editTextTagName.text.toString())
            resultIntent.putExtra(EXTRA_UPDATED_CONTENT, editTextContent.text.toString())
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    companion object {
        const val EXTRA_TAG = "extra_tag"
        const val EXTRA_CONTENT = "extra_content" // Add this line if you're passing content
        const val EXTRA_UPDATED_TAG = "extra_updated_tag"
        const val EXTRA_UPDATED_CONTENT = "extra_updated_content"
        const val EXTRA_DELETED_TAG = "extra_deleted_tag"
    }
}
