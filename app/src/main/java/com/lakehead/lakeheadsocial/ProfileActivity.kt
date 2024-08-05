package com.lakehead.lakeheadsocial

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : BaseActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_profile_content, findViewById(R.id.container))

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val profileImageView = findViewById<ImageView>(R.id.profileImageView)
        val nameTextView = findViewById<TextView>(R.id.nameTextView)
        val emailTextView = findViewById<TextView>(R.id.emailTextView)
        val bioTextView = findViewById<TextView>(R.id.bioTextView)

        val currentUser = auth.currentUser
        if (currentUser != null) {
            db.collection("users").document(currentUser.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val user = document.toObject(User::class.java)
                        if (user != null) {
                            nameTextView.text = user.name
                            emailTextView.text = user.email
                            bioTextView.text = user.bio
                            user.profilePictureUrl?.let {
                                Glide.with(this).load(it).into(profileImageView)
                            }
                        }
                    }
                }
                .addOnFailureListener { e ->
                    // Handle error
                }
        }
    }

    private fun logoutUser() {
        auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
