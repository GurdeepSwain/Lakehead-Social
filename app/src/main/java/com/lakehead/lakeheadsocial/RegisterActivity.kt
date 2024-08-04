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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    private var profileImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        val registerButton = findViewById<Button>(R.id.registerButton)
        val selectImageButton = findViewById<Button>(R.id.selectImageButton)
        val profileImageView = findViewById<ImageView>(R.id.profileImageView)

        selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }

        registerButton.setOnClickListener {
            val name = findViewById<EditText>(R.id.nameEditText).text.toString()
            val email = findViewById<EditText>(R.id.emailEditText).text.toString()
            val password = findViewById<EditText>(R.id.passwordEditText).text.toString()
            val bio = findViewById<EditText>(R.id.bioEditText).text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && bio.isNotEmpty()) {
                registerUser(name, email, password, bio)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null) {
            profileImageUri = data.data
            val profileImageView = findViewById<ImageView>(R.id.profileImageView)
            profileImageView.setImageURI(profileImageUri)
        }
    }

    private fun registerUser(name: String, email: String, password: String, bio: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: ""
                    if (profileImageUri != null) {
                        uploadProfileImage(userId, name, email, bio)
                    } else {
                        saveUserToFirestore(User(userId, name, email, bio, null))
                    }
                } else {
                    Toast.makeText(baseContext, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun uploadProfileImage(userId: String, name: String, email: String, bio: String) {
        val profileImageRef = storage.reference.child("profileImages/$userId-${UUID.randomUUID()}")
        profileImageUri?.let { uri ->
            profileImageRef.putFile(uri)
                .addOnSuccessListener {
                    profileImageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        saveUserToFirestore(User(userId, name, email, bio, downloadUrl.toString()))
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to upload profile image: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun saveUserToFirestore(user: User) {
        db.collection("users").document(user.uid).set(user)
            .addOnSuccessListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error saving profile: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        private const val REQUEST_IMAGE_PICK = 1001
    }
}
