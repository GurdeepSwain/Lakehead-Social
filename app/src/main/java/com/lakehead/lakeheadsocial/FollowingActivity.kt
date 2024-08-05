package com.lakehead.lakeheadsocial

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FollowingActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var postAdapter: PostAdapter
    private lateinit var postList: MutableList<Post>
    private lateinit var followingList: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_following)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        postList = mutableListOf()
        postAdapter = PostAdapter(postList)
        followingList = mutableListOf()

        val recyclerView = findViewById<RecyclerView>(R.id.followingPostsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = postAdapter

        loadFollowingPosts()
    }

    private fun loadFollowingPosts() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            db.collection("users").document(currentUser.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val user = document.toObject(User::class.java)
                        if (user != null) {
                            followingList.addAll(user.following)
                            getFollowingPosts()
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error loading following list: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun getFollowingPosts() {
        if (followingList.isNotEmpty()) {
            db.collection("posts")
                .whereIn("userId", followingList)
                .orderBy("timestamp")
                .get()
                .addOnSuccessListener { documents ->
                    postList.clear()
                    for (document in documents) {
                        val post = document.toObject(Post::class.java)
                        postList.add(post)
                    }
                    postAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error loading posts: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
