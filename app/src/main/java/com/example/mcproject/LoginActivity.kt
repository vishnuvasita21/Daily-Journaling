package com.example.mcproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mcproject.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth=FirebaseAuth.getInstance()
        binding.textView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.signinButton.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val password = binding.passET.text.toString()
            println("Reached here:1")
            if (email.isNotEmpty() && password.isNotEmpty()) {
                println("Reached here:2")
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            println("Loggedin successfully")
                            Toast.makeText(this,"Logged in successfully!",Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            println("Reached here:3")
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }
                    }


            } else {
                println("Reached here:4")
                Toast.makeText(this, "Please enter all the mandatory fields", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


}