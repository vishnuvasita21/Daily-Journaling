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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.text.Paragraph
import androidx.compose.ui.text.font.Font
import androidx.core.app.ActivityCompat
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
            createPDF(content)
        }

    }

    fun createPDF(content: String) {
        // creating a document
        val pdfDocument = PdfDocument()

        // creating page for document
        val pageInfo = PageInfo.Builder(1080, 1920, 1).create()

        val startPage = pdfDocument.startPage(pageInfo)

        val canvas = startPage.canvas

//        val paint: android.graphics.Paint = android.graphics.Paint()
//        paint.textSize = 15F
        val textPaint = TextPaint()
        textPaint.textSize = 35F

        // https://medium.com/over-engineering/drawing-multiline-text-to-canvas-on-android-9b98f0bfa16a
        val staticLayout = StaticLayout.Builder
            .obtain(content,0 , content.length, textPaint, 900)
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setLineSpacing(10f, 0.5f)
            .setIndents(intArrayOf(40), intArrayOf(40))
            .build()

//        canvas.drawText(content, 10F, 10F, paint)
        staticLayout.draw(canvas)
        pdfDocument.finishPage(startPage)

        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Test.pdf")

        try {
            pdfDocument.writeTo(FileOutputStream(file))
        } catch (e: Exception) {
            Log.e("d", "Error creatinf ile" + e.toString())
        }

        pdfDocument.close()
    }

    fun requestPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_CODE)
    }
}