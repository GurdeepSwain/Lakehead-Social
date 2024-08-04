package com.lakehead.lakeheadsocial

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PostAdapter(private val postList: List<Post>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_TEXT = 0
        private const val TYPE_IMAGE = 1
        private const val TYPE_VIDEO = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (postList[position].type) {
            "image" -> TYPE_IMAGE
            "video" -> TYPE_VIDEO
            else -> TYPE_TEXT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_IMAGE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post_image, parent, false)
                ImagePostViewHolder(view)
            }
            TYPE_VIDEO -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post_video, parent, false)
                VideoPostViewHolder(view)
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
            is VideoPostViewHolder -> holder.bind(post)
        }
    }

    override fun getItemCount() = postList.size

    class TextPostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val postTextView: TextView = itemView.findViewById(R.id.postTextView)

        fun bind(post: Post) {
            postTextView.text = post.text
        }
    }

    class ImagePostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val postTextView: TextView = itemView.findViewById(R.id.postTextView)
        private val postImageView: ImageView = itemView.findViewById(R.id.postImageView)

        fun bind(post: Post) {
            postTextView.text = post.text
            Glide.with(itemView.context).load(post.mediaUrl).into(postImageView)
        }
    }

    class VideoPostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val postTextView: TextView = itemView.findViewById(R.id.postTextView)
        private val postVideoView: VideoView = itemView.findViewById(R.id.postVideoView)

        fun bind(post: Post) {
            postTextView.text = post.text
            postVideoView.setVideoPath(post.mediaUrl)
            postVideoView.start()
        }
    }
}
