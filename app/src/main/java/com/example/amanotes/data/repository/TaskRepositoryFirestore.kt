package com.example.amanotes.data.repository

import com.example.amanotes.data.firestore.FirestoreManager
import com.example.amanotes.data.firestore.Task
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class TaskRepositoryFirestore {
    
    private val firestore = FirestoreManager.getFirestore()
    private val collection = firestore.collection("tasks")
    
    fun getAllTasks(userId: String): Flow<List<Task>> = callbackFlow {
        val listener = collection
            .whereEqualTo("userId", userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                val tasks = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Task::class.java)?.apply {
                        id = doc.id
                    }
                } ?: emptyList()
                
                trySend(tasks)
            }
        
        awaitClose { listener.remove() }
    }
    
    fun getTasksByCompletion(userId: String, isCompleted: Boolean): Flow<List<Task>> = callbackFlow {
        val listener = collection
            .whereEqualTo("userId", userId)
            .whereEqualTo("isCompleted", isCompleted)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                val tasks = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Task::class.java)?.apply {
                        id = doc.id
                    }
                } ?: emptyList()
                
                trySend(tasks)
            }
        
        awaitClose { listener.remove() }
    }
    
    suspend fun insertTask(userId: String, title: String, projectId: String? = null): String {
        val task = Task(
            userId = userId,
            title = title,
            projectId = projectId,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        
        val docRef = collection.add(task).await()
        return docRef.id
    }
    
    suspend fun updateTask(task: Task) {
        val updatedTask = task.copy(updatedAt = System.currentTimeMillis())
        collection.document(task.id)
            .set(updatedTask)
            .await()
    }
    
    suspend fun deleteTask(taskId: String) {
        collection.document(taskId).delete().await()
    }
    
    suspend fun toggleTaskCompletion(task: Task) {
        val updatedTask = task.copy(
            isCompleted = !task.isCompleted,
            updatedAt = System.currentTimeMillis()
        )
        collection.document(task.id)
            .set(updatedTask)
            .await()
    }
    
    suspend fun deleteCompletedTasks(userId: String) {
        val snapshot = collection
            .whereEqualTo("userId", userId)
            .whereEqualTo("isCompleted", true)
            .get()
            .await()
        
        snapshot.documents.forEach { doc ->
            doc.reference.delete().await()
        }
    }
    
    suspend fun getTaskById(id: String): Task? {
        return try {
            val doc = collection.document(id).get().await()
            doc.toObject(Task::class.java)?.apply {
                this.id = doc.id
            }
        } catch (e: Exception) {
            null
        }
    }
}

