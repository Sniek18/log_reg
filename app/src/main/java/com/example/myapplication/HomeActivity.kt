package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.squareup.picasso.Picasso

class HomeActivity : AppCompatActivity() {
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val homeIdTextView: TextView = findViewById(R.id.homeIdTextView)
        val homeUsernameTextView: TextView = findViewById(R.id.homeUsernameTextView)
        val homeEmailTextView: TextView = findViewById(R.id.homeEmailTextView)
        val homeProfilePictureImageView: ImageView = findViewById(R.id.homeProfilePictureImageView)

        db.collection("users").document(auth.currentUser!!.uid).get()
            .addOnSuccessListener { result->
                homeIdTextView.text = auth.currentUser!!.uid
                homeUsernameTextView.text = result.getString("username")
                homeEmailTextView.text = result.getString("email")
                Picasso.get().load(result.getString("photoURL")).into(homeProfilePictureImageView)
            }

        val homeLogoutButton: Button = findViewById(R.id.homeLogoutButton)
        homeLogoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            finish()
            startActivity(intent)
        }
    }
}