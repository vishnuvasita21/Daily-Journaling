package com.example.mcproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Button
import android.view.View
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import java.io.File

class ViewJournal : AppCompatActivity() {

    var PERMISSION_CODE = 1

    // view-model for saving pdf
    private lateinit var exportViewModel: ExportViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_journal)

        requestPermissions()

        // accept data from JournalFragment and accept the Journal object.
        val journal = intent.getSerializableExtra("journal") as Journal

        // get objects for entities in activity_view_journal
        val textTitle = findViewById<TextView>(R.id.textTitle)
        val textContent = findViewById<TextView>(R.id.textContent)
        val backButton = findViewById<Button>(R.id.backButton)
        val exportPdfButton = findViewById<Button>(R.id.export_pdf_button)
        val imageView = findViewById<ImageView>(R.id.view_image)

        // creating view-model from view-model factory
        exportViewModel = ViewModelProvider(this, ExportViewModelFactory())[ExportViewModel::class.java]

        // assign text title to textTitle entity from the layout
        textTitle.text = journal.title

        // assign text content to textContent entity from layout.
        textContent.text = journal.content

        // setup back button to go to previous activity in stack
        backButton.setOnClickListener {
            finish()
        }

        // saving and opening journal as pdf
        exportPdfButton.setOnClickListener {
            val pdfFile = exportViewModel.createPDF(journal)
            launchPDF(pdfFile)
        }

        // setting image if journal has image
        if(journal.imageUrl.isNotEmpty()) {
            imageView.visibility = View.VISIBLE
            imageView.setImageURI(Uri.parse(journal.imageUrl))
        } else {
            imageView.visibility = View.GONE
        }
    }

    /**
     * @param file the pdf file to open
     * launches the pdf file
     */
    fun launchPDF(file: File) {
        // getting uri
        val pdfUrl = FileProvider.getUriForFile(this, "${packageName}.provider", file)
        // creating intent
        val intent = Intent(Intent.ACTION_VIEW)
        // setting pdf as type
        intent.setDataAndType(pdfUrl, "application/pdf")
        // adding flag for read permission
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        // starting intent
        try {
            startActivity(intent)
        } catch (e: Exception) {
            // displaying toast if error in opening pdf
            Toast.makeText(this, "Error opening pdf.", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * requesting permission for reading and writing to external storage
     */
    fun requestPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_CODE)
    }


}