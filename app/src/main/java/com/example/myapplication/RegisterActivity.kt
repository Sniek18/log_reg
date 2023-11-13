package com.example.myapplication

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage

class RegisterActivity : AppCompatActivity() {
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private val storage = Firebase.storage.reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val registerButton: Button = findViewById(R.id.registerButton)
        val registerEmailEditText: EditText = findViewById(R.id.registerEmailEditText)
        val registerPasswordEditText: EditText = findViewById(R.id.registerPasswordEditText)
        val registerUsernameEditText: EditText = findViewById(R.id.registerUsernameEditText)
        val registerAlreadyTextView: TextView = findViewById(R.id.registerAlreadyTextView)
        val registerSelectProfilePictureButton: Button = findViewById(R.id.registerSelectProfilePictureButton)
        var profilePicUri: Uri? = null

        val mediaPicker = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null)
            {
                profilePicUri = uri
            }
        }

        registerSelectProfilePictureButton.setOnClickListener {
            mediaPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        registerButton.setOnClickListener {
            if(
                registerEmailEditText.text.toString() != "" &&
                registerPasswordEditText.text.toString() != "" &&
                registerUsernameEditText.text.toString() != "" &&
                profilePicUri != null
            )
            {
                auth.createUserWithEmailAndPassword(registerEmailEditText.text.toString(), registerPasswordEditText.text.toString())
                    .addOnCompleteListener { task->
                        if (task.isSuccessful)
                        {
                            val user = hashMapOf(
                                "username" to registerUsernameEditText.text.toString(),
                                "email" to registerEmailEditText.text.toString(),
                                "photoURL" to it.toString()
                            )
                            val imageRef = storage.child("profilePictures/${auth.currentUser!!.uid}")
                            imageRef.putFile(profilePicUri!!).onSuccessTask { _->
                                imageRef.downloadUrl.onSuccessTask {
                                    db.collection("users").document(auth.currentUser!!.uid).set(user)
                                        .addOnCompleteListener {result->
                                        if(result.isSuccessful)
                                        {
                                            val intent = Intent(this, HomeActivity::class.java)
                                            finish()
                                            startActivity(intent)
                                        }
                                    }
                                }
                            }
                        }
                    }

            }
        }

        registerAlreadyTextView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}