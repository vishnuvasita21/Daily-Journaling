package com.example.mcproject

import android.content.Intent
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.text.Paragraph
import androidx.compose.ui.text.font.Font
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.util.jar.Manifest

class ExportActivity : AppCompatActivity() {

    var PERMISSION_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_export)

        requestPermissions()

        val inputText = findViewById<EditText>(R.id.input_text)

        val exportButton = findViewById<Button>(R.id.export_button)
        val photoPickerButton = findViewById<Button>(R.id.pick_photo_button)

        val pickedPhoto = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
            } else {
                Log.d("PhotoPicker", "No Media Selected")
            }
        }

        // https://dayoneapp.com/blog/journaling-examples/#5-daily-reflections-journal
        inputText.setText("Date: April 27, 2022\n" +
                "\n" +
                "Today was a busy day at work. I had a lot of meetings and deadlines to meet, which kept me on my toes all day. I felt a little bit stressed at times, but overall, I was able to stay focused and get everything done that needed to be done.\n" +
                "\n" +
                "During my lunch break, I went for a walk around the park near my office. It was a beautiful day outside, and I felt grateful for the opportunity to get some fresh air and sunshine. As I walked, I listened to a podcast about mindfulness and tried to focus on being present in the moment.\n" +
                "\n" +
                "After work, I met up with a friend for dinner. We caught up on each other’s lives and talked about our plans for the future. It was nice to have some social time after a busy day at work.\n" +
                "\n" +
                "Before bed, I took some time to reflect on the day. I thought about the things that went well and the areas where I could improve. I also wrote down some goals for tomorrow, including getting up a little bit earlier and taking a few moments to meditate before starting my day.\n" +
                "\n" +
                "Overall, today was a good day. I feel grateful for the opportunities and experiences that came my way, and I’m looking forward to what tomorrow will bring.")

        exportButton.setOnClickListener {
            val content = inputText.text.toString()
//            createPDF(content)
        }

        photoPickerButton.setOnClickListener {
            pickedPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

    }

    fun launchPDF(file: File) {
        val pdfUrl = FileProvider.getUriForFile(this, "${packageName}.provider", file)

        val intent = Intent(Intent.ACTION_VIEW)

        intent.setDataAndType(pdfUrl, "application/pdf")

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        try {
            startActivity(intent)
        } catch (e: Exception) {
            Log.e("ee", e.toString())
        }
    }

    fun requestPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_CODE)
    }
}