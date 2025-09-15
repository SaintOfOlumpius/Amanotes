package com.example.amanotes.data.repository

import com.example.amanotes.data.local.ProjectDao
import com.example.amanotes.data.local.ProjectEntity
import com.example.amanotes.data.local.ProjectStatus
import com.example.amanotes.data.local.ProjectPriority
import kotlinx.coroutines.flow.Flow

class ProjectRepository(private val projectDao: ProjectDao) {
    fun getAllProjects(): Flow<List<ProjectEntity>> = projectDao.getAllProjects()
    
    fun getProjectsByStatus(status: ProjectStatus): Flow<List<ProjectEntity>> = 
        projectDao.getProjectsByStatus(status)
    
    fun getProjectsByPriority(priority: ProjectPriority): Flow<List<ProjectEntity>> = 
        projectDao.getProjectsByPriority(priority)
    
    fun getProjectsDueSoon(daysAhead: Int = 7): Flow<List<ProjectEntity>> {
        val timestamp = System.currentTimeMillis() + (daysAhead * 24 * 60 * 60 * 1000L)
        return projectDao.getProjectsDueSoon(timestamp)
    }
    
    fun searchProjects(query: String): Flow<List<ProjectEntity>> = 
        projectDao.searchProjects(query)
    
    suspend fun insertProject(
        title: String,
        description: String = "",
        status: ProjectStatus = ProjectStatus.PLANNING,
        priority: ProjectPriority = ProjectPriority.MEDIUM,
        dueDate: Long? = null
    ): Long {
        val project = ProjectEntity(
            title = title,
            description = description,
            status = status,
            priority = priority,
            dueDate = dueDate
        )
        return projectDao.insertProject(project)
    }
    
    suspend fun updateProject(project: ProjectEntity) {
        val updatedProject = project.copy(updatedAt = System.currentTimeMillis())
        projectDao.updateProject(updatedProject)
    }
    
    suspend fun deleteProject(project: ProjectEntity) {
        projectDao.deleteProject(project)
    }
    
    suspend fun updateProgress(project: ProjectEntity, progress: Float) {
        projectDao.updateProgress(project.id, progress.coerceIn(0f, 1f))
        
        // Auto-complete if progress reaches 100%
        if (progress >= 1f && project.status != ProjectStatus.COMPLETED) {
            projectDao.updateStatus(project.id, ProjectStatus.COMPLETED)
        }
    }
    
    suspend fun updateStatus(project: ProjectEntity, status: ProjectStatus) {
        projectDao.updateStatus(project.id, status)
        
        // Reset progress if moving back from completed
        if (project.status == ProjectStatus.COMPLETED && status != ProjectStatus.COMPLETED) {
            projectDao.updateProgress(project.id, 0.95f) // Almost complete but not 100%
        }
    }
    
    suspend fun deleteCompletedProjects() {
        projectDao.deleteProjectsByStatus(ProjectStatus.COMPLETED)
    }
}
