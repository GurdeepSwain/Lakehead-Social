package com.lakehead.lakeheadsocial

data class Post(
    val text: String = "",
    val userId: String = "",
    val type: String = "text", // "text", "image", or "video"
    val mediaUrl: String? = null // URL for image or video
)
