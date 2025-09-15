package com.example.amanotes.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val status: ProjectStatus = ProjectStatus.IN_PROGRESS,
    val priority: ProjectPriority = ProjectPriority.MEDIUM,
    val progress: Float = 0f,
    val dueDate: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

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
