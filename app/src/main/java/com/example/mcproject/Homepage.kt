package com.example.mcproject

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
class Homepage : AppCompatActivity() {
    private lateinit var btnTransparent: Button
    private lateinit var myTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)
        myTextView= findViewById(R.id.dailJournalTextView)
        btnTransparent = findViewById(R.id.signinButton)
        // Create ObjectAnimator for the fade-in animation
        val fadeIn = ObjectAnimator.ofFloat(myTextView, "alpha", 0f, 1f)
        val fadeInButton = ObjectAnimator.ofFloat(btnTransparent, "alpha", 0f, 1f)
        // Duration of the animation in milliseconds
        fadeIn.duration = 2000
        fadeInButton.duration=5000
        // Start the animation to make the text gradually fade in
        fadeIn.start()
        fadeInButton.start()

        btnTransparent.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                startActivity(Intent(this@Homepage, SignUpActivity::class.java))
            }
        })
    }
}