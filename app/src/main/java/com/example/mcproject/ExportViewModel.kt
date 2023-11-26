package com.example.mcproject

import android.graphics.Color
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class ExportViewModel : ViewModel() {

    val isErrorOccurred = MutableLiveData<Boolean>()
    var job: Job? = null

    fun createPDF(journal: Journal): File {
        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), journal.title+".pdf")

        job = CoroutineScope(Dispatchers.IO).launch {
            // creating a document
            val pdfDocument = PdfDocument()

            withContext(Dispatchers.IO) {
                // creating page for document
                val pageInfo = PdfDocument.PageInfo.Builder(1080, 1920, 1).create()

                val startPage = pdfDocument.startPage(pageInfo)

                val canvas = startPage.canvas

                val headingPaint = TextPaint()
                headingPaint.textSize = 50F
                headingPaint.color = Color.BLUE

                canvas.drawText(journal.title, 100F, 100F, headingPaint)

                val textPaint = TextPaint()
                textPaint.textSize = 35F

                val staticLayout = StaticLayout.Builder
                    .obtain(journal.content,0 , journal.content.length, textPaint, 900)
                    .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                    .setLineSpacing(10f, 0.5f)
                    .setIndents(intArrayOf(40), intArrayOf(40))
                    .build()

                canvas.translate(50F, 150F)

                staticLayout.draw(canvas)
                pdfDocument.finishPage(startPage)
            }

            try {
                withContext(Dispatchers.IO) {
                    pdfDocument.writeTo(FileOutputStream(file))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    isErrorOccurred.postValue(true)
                }
                Log.e("VMVM", "Error creating ile" + e.printStackTrace())
            }
            pdfDocument.close()

        }
        return file
    }

    override fun onCleared() {
        job?.cancel()
        super.onCleared()
    }

}