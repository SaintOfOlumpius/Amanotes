package com.example.amanotes.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects ORDER BY updatedAt DESC")
    fun getAllProjects(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE status = :status ORDER BY updatedAt DESC")
    fun getProjectsByStatus(status: ProjectStatus): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE priority = :priority ORDER BY updatedAt DESC")
    fun getProjectsByPriority(priority: ProjectPriority): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE dueDate IS NOT NULL AND dueDate <= :timestamp ORDER BY dueDate ASC")
    fun getProjectsDueSoon(timestamp: Long): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' ORDER BY updatedAt DESC")
    fun searchProjects(query: String): Flow<List<ProjectEntity>>

    @Insert
    suspend fun insertProject(project: ProjectEntity): Long

    @Update
    suspend fun updateProject(project: ProjectEntity)

    @Delete
    suspend fun deleteProject(project: ProjectEntity)

    @Query("UPDATE projects SET progress = :progress WHERE id = :id")
    suspend fun updateProgress(id: Long, progress: Float)

    @Query("UPDATE projects SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Long, status: ProjectStatus)

    @Query("DELETE FROM projects WHERE status = :status")
    suspend fun deleteProjectsByStatus(status: ProjectStatus)
}
