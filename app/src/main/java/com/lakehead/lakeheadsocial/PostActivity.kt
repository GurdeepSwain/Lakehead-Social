package com.lakehead.lakeheadsocial

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PostActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val postButton = findViewById<Button>(R.id.postButton)
        postButton.setOnClickListener {
            val postText = findViewById<EditText>(R.id.postEditText).text.toString()
            if (postText.isNotEmpty()) {
                createPost(postText)
            } else {
                Toast.makeText(this, "Please enter text for the post", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createPost(postText: String) {
        val post = Post(postText, auth.currentUser?.uid ?: "")

        db.collection("posts").add(post)
            .addOnSuccessListener {
                Toast.makeText(this, "Post added", Toast.LENGTH_SHORT).show()
                Log.d("PostActivity", "Post added successfully, navigating to MainActivity")
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error adding post: ${exception.message}", Toast.LENGTH_SHORT).show()
                Log.e("PostActivity", "Error adding post: ${exception.message}")
            }
    }
}
