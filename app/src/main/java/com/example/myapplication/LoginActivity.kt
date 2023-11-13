package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {
    private val auth = Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        auth.addAuthStateListener { newAuth ->
            if(newAuth.currentUser != null)
            {
                val intent = Intent(this, HomeActivity::class.java)
                finish()
                startActivity(intent)
            }
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginGoToTextView: TextView = findViewById(R.id.loginGoToTextView)
        val loginButton: Button = findViewById(R.id.loginButton)
        val loginEmailEditText: EditText = findViewById(R.id.loginEmailEditText)
        val loginPasswordEditText: EditText = findViewById(R.id.loginPasswordEditText)

        loginGoToTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            auth.signInWithEmailAndPassword(loginEmailEditText.text.toString(), loginPasswordEditText.text.toString())
                .addOnCompleteListener {task ->
                    if(task.isSuccessful)
                    {
                        val intent = Intent(this, HomeActivity::class.java)
                        finish()
                        startActivity(intent)
                    }
                }
        }
    }
}