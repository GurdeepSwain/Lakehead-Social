package com.lakehead.lakeheadsocial

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class PostActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        val postButton = findViewById<Button>(R.id.postButton)
        val addImageButton = findViewById<Button>(R.id.addImageButton)
        val postEditText = findViewById<EditText>(R.id.postEditText)
        val imageView = findViewById<ImageView>(R.id.imageView)

        addImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }

        postButton.setOnClickListener {
            val text = postEditText.text.toString()
            if (text.isNotEmpty()) {
                if (imageUri != null) {
                    uploadPostWithImage(text)
                } else {
                    uploadTextPost(text)
                }
            } else {
                Toast.makeText(this, "Please enter text", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadTextPost(text: String) {
        val userId = auth.currentUser?.uid ?: return
        val postId = UUID.randomUUID().toString()
        val currentUserRef = db.collection("users").document(userId)

        currentUserRef.get().addOnSuccessListener { document ->
            if (document != null) {
                val userName = document.getString("name") ?: "Unknown"
                val post = Post(
                    id = postId,
                    userId = userId,
                    userName = userName,
                    text = text,
                    timestamp = Timestamp.now()
                )

                db.collection("posts").document(postId).set(post)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Post uploaded", Toast.LENGTH_SHORT).show()
                        finish()  // Go back to the previous activity
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Failed to upload post: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun uploadPostWithImage(text: String) {
        val userId = auth.currentUser?.uid ?: return
        val postId = UUID.randomUUID().toString()
        val postImageRef = storage.reference.child("postImages/$postId")
        val currentUserRef = db.collection("users").document(userId)

        currentUserRef.get().addOnSuccessListener { document ->
            if (document != null) {
                val userName = document.getString("name") ?: "Unknown"
                imageUri?.let { uri ->
                    postImageRef.putFile(uri)
                        .addOnSuccessListener {
                            postImageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                                val post = Post(
                                    id = postId,
                                    userId = userId,
                                    userName = userName,
                                    text = text,
                                    imageUrl = downloadUrl.toString(),
                                    timestamp = Timestamp.now()
                                )
                                db.collection("posts").document(postId).set(post)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Post uploaded", Toast.LENGTH_SHORT).show()
                                        finish()  // Go back to the previous activity
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, "Failed to upload post: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Failed to upload image: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data
            val imageView = findViewById<ImageView>(R.id.imageView)
            imageView.setImageURI(imageUri)
        }
    }

    companion object {
        private const val REQUEST_IMAGE_PICK = 1001
    }
}
