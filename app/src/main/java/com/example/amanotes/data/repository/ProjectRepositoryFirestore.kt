package com.example.amanotes.data.repository

import com.example.amanotes.data.firestore.FirestoreManager
import com.example.amanotes.data.firestore.Project
import com.example.amanotes.data.firestore.ProjectPriority
import com.example.amanotes.data.firestore.ProjectStatus
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ProjectRepositoryFirestore {
    
    private val firestore = FirestoreManager.getFirestore()
    private val collection = firestore.collection("projects")
    
    fun getAllProjects(userId: String): Flow<List<Project>> = callbackFlow {
        val listener = collection
            .whereEqualTo("userId", userId)
            .orderBy("updatedAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                val projects = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Project::class.java)?.apply {
                        id = doc.id
                    }
                } ?: emptyList()
                
                trySend(projects)
            }
        
        awaitClose { listener.remove() }
    }
    
    fun getProjectsByStatus(userId: String, status: ProjectStatus): Flow<List<Project>> = callbackFlow {
        val listener = collection
            .whereEqualTo("userId", userId)
            .whereEqualTo("status", status.name)
            .orderBy("updatedAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                val projects = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Project::class.java)?.apply {
                        id = doc.id
                    }
                } ?: emptyList()
                
                trySend(projects)
            }
        
        awaitClose { listener.remove() }
    }
    
    fun getProjectsByPriority(userId: String, priority: ProjectPriority): Flow<List<Project>> = callbackFlow {
        val listener = collection
            .whereEqualTo("userId", userId)
            .whereEqualTo("priority", priority.name)
            .orderBy("updatedAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                val projects = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Project::class.java)?.apply {
                        id = doc.id
                    }
                } ?: emptyList()
                
                trySend(projects)
            }
        
        awaitClose { listener.remove() }
    }
    
    fun getProjectsDueSoon(userId: String, daysAhead: Int = 7): Flow<List<Project>> = callbackFlow {
        val timestamp = System.currentTimeMillis() + (daysAhead * 24 * 60 * 60 * 1000L)
        val listener = collection
            .whereEqualTo("userId", userId)
            .whereLessThanOrEqualTo("dueDate", timestamp)
            .whereNotEqualTo("dueDate", null)
            .orderBy("dueDate", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                val projects = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Project::class.java)?.apply {
                        id = doc.id
                    }
                } ?: emptyList()
                
                trySend(projects)
            }
        
        awaitClose { listener.remove() }
    }
    
    fun searchProjects(userId: String, query: String): Flow<List<Project>> = callbackFlow {
        val listener = collection
            .whereEqualTo("userId", userId)
            .orderBy("updatedAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                val projects = snapshot?.documents
                    ?.mapNotNull { doc ->
                        doc.toObject(Project::class.java)?.apply {
                            id = doc.id
                        }
                    }
                    ?.filter { project ->
                        project.title.contains(query, ignoreCase = true) ||
                        project.description.contains(query, ignoreCase = true)
                    } ?: emptyList()
                
                trySend(projects)
            }
        
        awaitClose { listener.remove() }
    }
    
    suspend fun insertProject(
        userId: String,
        title: String,
        description: String = "",
        status: ProjectStatus = ProjectStatus.PLANNING,
        priority: ProjectPriority = ProjectPriority.MEDIUM,
        dueDate: Long? = null
    ): String {
        val project = Project(
            userId = userId,
            title = title,
            description = description,
            status = status.name,
            priority = priority.name,
            dueDate = dueDate,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        
        val docRef = collection.add(project).await()
        return docRef.id
    }
    
    suspend fun updateProject(project: Project) {
        val updatedProject = project.copy(updatedAt = System.currentTimeMillis())
        collection.document(project.id)
            .set(updatedProject)
            .await()
    }
    
    suspend fun deleteProject(projectId: String) {
        collection.document(projectId).delete().await()
    }
    
    suspend fun updateProgress(project: Project, progress: Float) {
        val newProgress = progress.coerceIn(0f, 1f)
        val updatedStatus = if (newProgress >= 1f && project.status != ProjectStatus.COMPLETED.name) {
            ProjectStatus.COMPLETED.name
        } else {
            project.status
        }
        
        val updatedProject = project.copy(
            progress = newProgress,
            status = updatedStatus,
            updatedAt = System.currentTimeMillis()
        )
        collection.document(project.id)
            .set(updatedProject)
            .await()
    }
    
    suspend fun updateStatus(project: Project, status: ProjectStatus) {
        val newProgress = if (project.status == ProjectStatus.COMPLETED.name && status != ProjectStatus.COMPLETED) {
            0.95f
        } else {
            project.progress
        }
        
        val updatedProject = project.copy(
            status = status.name,
            progress = newProgress,
            updatedAt = System.currentTimeMillis()
        )
        collection.document(project.id)
            .set(updatedProject)
            .await()
    }
    
    suspend fun deleteCompletedProjects(userId: String) {
        val snapshot = collection
            .whereEqualTo("userId", userId)
            .whereEqualTo("status", ProjectStatus.COMPLETED.name)
            .get()
            .await()
        
        snapshot.documents.forEach { doc ->
            doc.reference.delete().await()
        }
    }
    
    suspend fun getProjectById(id: String): Project? {
        return try {
            val doc = collection.document(id).get().await()
            doc.toObject(Project::class.java)?.apply {
                this.id = doc.id
            }
        } catch (e: Exception) {
            null
        }
    }
}

