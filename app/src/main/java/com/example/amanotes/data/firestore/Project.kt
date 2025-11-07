package com.example.amanotes.data.firestore

enum class ProjectStatus(val displayName: String) {
    PLANNING("Planning"),
    IN_PROGRESS("In Progress"),
    ON_HOLD("On Hold"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled")
}

enum class ProjectPriority(val displayName: String) {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High"),
    URGENT("Urgent")
}

data class Project(
    var id: String = "",
    var userId: String = "",
    var title: String = "",
    var description: String = "",
    var status: String = ProjectStatus.IN_PROGRESS.name,
    var priority: String = ProjectPriority.MEDIUM.name,
    var progress: Float = 0f,
    var dueDate: Long? = null,
    var createdAt: Long = System.currentTimeMillis(),
    var updatedAt: Long = System.currentTimeMillis(),
    var thumbnailUrl: String? = null
) {
    constructor() : this(
        id = "",
        userId = "",
        title = "",
        description = "",
        status = ProjectStatus.IN_PROGRESS.name,
        priority = ProjectPriority.MEDIUM.name,
        progress = 0f,
        dueDate = null,
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis(),
        thumbnailUrl = null
    )
}

