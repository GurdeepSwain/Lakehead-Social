package com.lakehead.lakeheadsocial

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserAdapter(private val userList: List<User>, private val currentUser: User) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val followButton: Button = itemView.findViewById(R.id.followButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.nameTextView.text = user.name

        // Check if the current user is following this user
        if (currentUser.following.contains(user.uid)) {
            holder.followButton.text = "Unfollow"
        } else {
            holder.followButton.text = "Follow"
        }

        holder.followButton.setOnClickListener {
            val db = FirebaseFirestore.getInstance()
            if (currentUser.following.contains(user.uid)) {
                // Unfollow the user
                currentUser.following.remove(user.uid)
                db.collection("users").document(currentUser.uid).update("following", currentUser.following)
                holder.followButton.text = "Follow"
            } else {
                // Follow the user
                currentUser.following.add(user.uid)
                db.collection("users").document(currentUser.uid).update("following", currentUser.following)
                holder.followButton.text = "Unfollow"
            }
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}
