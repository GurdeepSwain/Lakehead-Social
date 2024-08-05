package com.lakehead.lakeheadsocial

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

class PostAdapter(private val postList: List<Post>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_TEXT = 0
        private const val TYPE_IMAGE = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (postList[position].imageUrl != null) TYPE_IMAGE else TYPE_TEXT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_IMAGE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post_image, parent, false)
                ImagePostViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post_text, parent, false)
                TextPostViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val post = postList[position]
        when (holder) {
            is TextPostViewHolder -> holder.bind(post)
            is ImagePostViewHolder -> holder.bind(post)
        }
    }

    override fun getItemCount() = postList.size

    class TextPostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val postTextView: TextView = itemView.findViewById(R.id.postTextView)
        private val userNameTextView: TextView = itemView.findViewById(R.id.userNameTextView)
        private val timestampTextView: TextView = itemView.findViewById(R.id.timestampTextView)

        fun bind(post: Post) {
            postTextView.text = post.text
            userNameTextView.text = post.userName
            timestampTextView.text = post.timestamp?.let { SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault()).format(it.toDate()) }
        }
    }

    class ImagePostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val postTextView: TextView = itemView.findViewById(R.id.postTextView)
        private val postImageView: ImageView = itemView.findViewById(R.id.postImageView)
        private val userNameTextView: TextView = itemView.findViewById(R.id.userNameTextView)
        private val timestampTextView: TextView = itemView.findViewById(R.id.timestampTextView)

        fun bind(post: Post) {
            postTextView.text = post.text
            if (post.imageUrl != null) {
                Log.d("PostAdapter", "Loading image: ${post.imageUrl}")
                Glide.with(itemView.context).load(post.imageUrl).into(postImageView)
                postImageView.visibility = View.VISIBLE
            } else {
                postImageView.visibility = View.GONE
            }
            userNameTextView.text = post.userName
            timestampTextView.text = post.timestamp?.let { SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault()).format(it.toDate()) }
        }
    }
}
