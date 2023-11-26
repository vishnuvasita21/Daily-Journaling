package com.example.mcproject

import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class JournalEntryActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private lateinit var entryEditText: EditText
    private lateinit var rememberButton: Button
    private lateinit var titleText: EditText
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var wordCountTextView: TextView
    private var isLocationEnabled: Boolean = false
    private var location: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal_entry)

        val locationToggleButton = findViewById<ToggleButton>(R.id.locationToggleButton)
        locationToggleButton.setOnClickListener {
            // Change a boolean variable value depending on the toggle state
            // For example:
            isLocationEnabled = locationToggleButton.isChecked
        }

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val journalFragment = JournalFragment()
        fragmentTransaction.add(R.id.searchFragment, journalFragment)
        fragmentTransaction.commit()


        var day = 26
        var month = 10
        var year = 2023
        val dateButton : Button = findViewById(R.id.DatePickerButton)
        dateButton.setOnClickListener {
            DatePickerDialog(this,this,year,month,day).show()
        }


        entryEditText = findViewById(R.id.textContent)
        rememberButton = findViewById(R.id.rememberButton)
        titleText = findViewById(R.id.textTitle)
        dbHelper = DatabaseHelper(this)
        wordCountTextView = findViewById(R.id.textView5)

        rememberButton.setOnClickListener {
            val titleContent: String = titleText.text.toString()
            val mainContent: String = entryEditText.text.toString()

            val currentDate = Calendar.getInstance().time// detect current date here
            val formattedDate = SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH).format(currentDate)
            if(this.isLocationEnabled){
                askForLocationPermission(); //end
            }else{
                this.location = "";
            }
            insertJournal(Journal(titleContent, mainContent, listOf("tag1", "tag2"), formattedDate, this.location))
            Toast.makeText(this, "Entry Stored successfully!", Toast.LENGTH_SHORT).show()
        }

        entryEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed for this example
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // Calculate the word count and update the textView5
                val wordCount = calculateWordCount(charSequence.toString())
                wordCountTextView.text = String.format("%d words", wordCount)
            }

            override fun afterTextChanged(editable: Editable?) {
                // Not needed for this example
            }
        })

        // If needed, you can clear the table in the database on each activity creation
        dbHelper.writableDatabase.delete(DatabaseHelper.TABLE_NAME, null, null)
    }

    private fun insertJournal(journal: Journal) {
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_TITLE, journal.title)
            put(DatabaseHelper.COLUMN_CONTENT, journal.content)
            put(DatabaseHelper.COLUMN_TAGS, journal.tags.joinToString(","))
            put(DatabaseHelper.COLUMN_DATE, journal.date) // Assuming date is a String in the format you want
            put(DatabaseHelper.COLUMN_LOC, journal.location)
        }

        val db = dbHelper.writableDatabase
        db.insert(DatabaseHelper.TABLE_NAME, null, values)
        db.close()
    }

    private fun askForLocationPermission() {
         val locationProvider = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationProvider.lastLocation.addOnSuccessListener { location ->
            this.location = "${location.latitude},${location.longitude}" // Save location in "latitude,longitude" format
         }
    }

    private fun calculateWordCount(text: String): Int {
        // Simple logic to count words (assuming words are separated by spaces)
        val words = text.split("\\s+".toRegex()).toTypedArray()
        return words.size
    }

    private fun filterByDate(formattedDate: String): Journal? {
        val db = dbHelper.readableDatabase
        val selectionArgs = arrayOf(formattedDate)
        val cursor = db.query(
            DatabaseHelper.TABLE_NAME,
            null,
            "${DatabaseHelper.COLUMN_DATE} = ?",
            selectionArgs,
            null,
            null,
            null
        )
        return if (cursor.moveToFirst()) {
            val title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CONTENT))
            val tagsString = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TAGS))
            val tags = tagsString.split(",")
            val location = getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LOC))
            Journal(title, content, tags, formattedDate, location)
        } else {
            null
        }
    }

    // Update the onDateSet method to convert the selected date into the desired format
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val selectedDate = Calendar.getInstance()
        selectedDate.set(year, month, dayOfMonth)
        val formattedDate = SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH).format(selectedDate.time)
        val journal = filterByDate(formattedDate)
            ?: return // Filter Journals from db with the formatted date
        val intent = Intent(this, ViewJournal::class.java)
        intent.putExtra("journal", journal)
        startActivity(intent)
    }
}
