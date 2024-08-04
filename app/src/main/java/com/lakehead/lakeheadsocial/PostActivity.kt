package com.lakehead.lakeheadsocial

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class PostActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage

    private var mediaUri: Uri? = null
    private var mediaType: String = "text" // "text", "image", or "video"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        val postButton = findViewById<Button>(R.id.postButton)
        val addImageButton = findViewById<Button>(R.id.addImageButton)
        val addVideoButton = findViewById<Button>(R.id.addVideoButton)

        postButton.setOnClickListener {
            val postText = findViewById<EditText>(R.id.postEditText).text.toString()
            if (postText.isNotEmpty() || mediaUri != null) {
                createPost(postText)
            } else {
                Toast.makeText(this, "Please enter text or add media for the post", Toast.LENGTH_SHORT).show()
            }
        }

        addImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }

        addVideoButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "video/*"
            startActivityForResult(intent, REQUEST_VIDEO_PICK)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            mediaUri = data.data
            mediaType = when (requestCode) {
                REQUEST_IMAGE_PICK -> "image"
                REQUEST_VIDEO_PICK -> "video"
                else -> "text"
            }
            Log.d("PostActivity", "Media URI: $mediaUri, Media Type: $mediaType")
        } else {
            Log.e("PostActivity", "Failed to pick media")
        }
    }

    private fun createPost(postText: String) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            if (mediaUri != null) {
                uploadMediaAndCreatePost(postText, currentUser.uid)
            } else {
                val post = Post(postText, currentUser.uid, "text", null)
                savePostToFirestore(post)
            }
        }
    }

    private fun uploadMediaAndCreatePost(postText: String, userId: String) {
        val mediaRef = storage.reference.child("posts/${UUID.randomUUID()}-${mediaUri?.lastPathSegment}")
        mediaUri?.let { uri ->
            mediaRef.putFile(uri)
                .addOnSuccessListener {
                    mediaRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        val post = Post(postText, userId, mediaType, downloadUrl.toString())
                        savePostToFirestore(post)
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to upload media: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("PostActivity", "Failed to upload media: ${e.message}")
                }
        } ?: run {
            Log.e("PostActivity", "Media URI is null")
            Toast.makeText(this, "Media URI is null", Toast.LENGTH_SHORT).show()
        }
    }

    private fun savePostToFirestore(post: Post) {
        db.collection("posts").add(post)
            .addOnSuccessListener {
                Toast.makeText(this, "Post added", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error adding post: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        private const val REQUEST_IMAGE_PICK = 1001
        private const val REQUEST_VIDEO_PICK = 1002
    }
}
