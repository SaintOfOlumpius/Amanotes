package com.example.amanotes.data.local

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromProjectStatus(status: ProjectStatus): String {
        return status.name
    }

    @TypeConverter
    fun toProjectStatus(status: String): ProjectStatus {
        return ProjectStatus.valueOf(status)
    }

    @TypeConverter
    fun fromProjectPriority(priority: ProjectPriority): String {
        return priority.name
    }

    @TypeConverter
    fun toProjectPriority(priority: String): ProjectPriority {
        return ProjectPriority.valueOf(priority)
    }
}
