package com.example.amanotes.data.firestore

data class User(
    var id: String = "",
    var userId: String = "", // Firebase/Google user ID
    var name: String = "",
    var email: String = "",
    var photoUrl: String? = null,
    var createdAt: Long = System.currentTimeMillis(),
    var updatedAt: Long = System.currentTimeMillis()
) {
    constructor() : this(
        id = "",
        userId = "",
        name = "",
        email = "",
        photoUrl = null,
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
    )
}

