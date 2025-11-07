package com.example.amanotes.data.firestore

data class Note(
    var id: String = "",
    
    var userId: String = "",
    var title: String = "",
    var content: String = "",
    var category: String = "General",
    var isFavorite: Boolean = false,
    var createdAt: Long = System.currentTimeMillis(),
    var updatedAt: Long = System.currentTimeMillis(),
    var attachmentUrl: String? = null
) {
    constructor() : this(
        id = "",
        userId = "",
        title = "",
        content = "",
        category = "General",
        isFavorite = false,
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis(),
        attachmentUrl = null
    )
}

