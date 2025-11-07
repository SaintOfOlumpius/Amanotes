package com.example.amanotes.data.firestore

data class Task(
    var id: String = "",
    var userId: String = "",
    var title: String = "",
    var isCompleted: Boolean = false,
    var projectId: String? = null,
    var createdAt: Long = System.currentTimeMillis(),
    var updatedAt: Long = System.currentTimeMillis()
) {
    constructor() : this(
        id = "",
        userId = "",
        title = "",
        isCompleted = false,
        projectId = null,
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
    )
}

