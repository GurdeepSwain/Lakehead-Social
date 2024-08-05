package com.lakehead.lakeheadsocial

import com.google.firebase.Timestamp

data class Post(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val text: String = "",
    val imageUrl: String? = null,
    val timestamp: Timestamp? = null
)
