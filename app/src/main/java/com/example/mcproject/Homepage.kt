package com.example.mcproject

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class Homepage : AppCompatActivity() {
    private lateinit var btnTransparent: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)
        btnTransparent = findViewById(R.id.signinButton)
        btnTransparent.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // Add your click logic here
                // For example, you can start a new activity or perform some other action
                // Replace the following line with your desired logic
                startActivity(Intent(this@Homepage, SignUpActivity::class.java))
            }
        })

        createNotificationChannel()

        // Create a periodic work request for the NotificationWorker
        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(24, TimeUnit.HOURS)
            .build()

        // Enqueue the work uniquely
        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork("intervalNotification", ExistingPeriodicWorkPolicy.KEEP, workRequest)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }

            // Get the NotificationManager from the system and create the channel
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}