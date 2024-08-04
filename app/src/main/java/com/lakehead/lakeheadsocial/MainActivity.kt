package com.lakehead.lakeheadsocial

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var postAdapter: PostAdapter
    private lateinit var postList: MutableList<Post>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        // Check if user is logged in
        if (auth.currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        db = FirebaseFirestore.getInstance()

        postList = mutableListOf()
        postAdapter = PostAdapter(postList)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = postAdapter

        loadPosts()

        val newPostButton = findViewById<Button>(R.id.newPostButton)
        newPostButton.setOnClickListener {
            val intent = Intent(this, PostActivity::class.java)
            startActivity(intent)
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Already on Home, no need to do anything
                    true
                }
                R.id.navigation_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_logout -> {
                    logoutUser()
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadPosts()  // Refresh the posts when returning to MainActivity
    }

    private fun loadPosts() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            db.collection("posts")
                .whereEqualTo("userId", currentUser.uid)
                .get()
                .addOnSuccessListener { documents ->
                    postList.clear()
                    for (document in documents) {
                        val post = document.toObject(Post::class.java)
                        postList.add(post)
                    }
                    postAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Error getting posts: ${exception.message}", Toast.LENGTH_SHORT).show()
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
