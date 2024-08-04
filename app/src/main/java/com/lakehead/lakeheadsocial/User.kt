package com.lakehead.lakeheadsocial

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val bio: String = "",
    val profilePictureUrl: String? = null
)
