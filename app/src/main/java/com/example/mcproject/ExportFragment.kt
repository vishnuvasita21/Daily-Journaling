package com.example.mcproject

import android.content.Intent
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.os.Bundle
import android.os.Environment
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File
import java.io.FileOutputStream

class ExportFragment : Fragment() {

    var PERMISSION_CODE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_export, container, false)

        requestPermissions()

        val exportButton = view.findViewById<Button>(R.id.export_button)
        val photoPickerButton = view.findViewById<Button>(R.id.pick_photo_button)

        val pickedPhoto = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
            } else {
                Log.d("PhotoPicker", "No Media Selected")
            }
        }

        exportButton.setOnClickListener {
            //val content = inputText.text.toString()
            //createPDF(content)
        }

        photoPickerButton.setOnClickListener {
            pickedPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        return view
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
            Log.e("d", "Error creating file" + e.toString())
        }

        pdfDocument.close()

        launchPDF(file)
    }

    fun launchPDF(file: File) {
        val pdfUrl = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.provider", file)

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
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_CODE)
    }
}