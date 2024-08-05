package com.lakehead.lakeheadsocial

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : BaseActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var postAdapter: PostAdapter
    private lateinit var postList: MutableList<Post>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_main_content, findViewById(R.id.container))

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        postList = mutableListOf()
        postAdapter = PostAdapter(postList)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = postAdapter

        loadPosts()

        val newPostButton = findViewById<Button>(R.id.newPostButton)
        newPostButton.setOnClickListener {
            startActivity(Intent(this, PostActivity::class.java))
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
}
