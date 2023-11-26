package com.example.mcproject

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
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
    }
}