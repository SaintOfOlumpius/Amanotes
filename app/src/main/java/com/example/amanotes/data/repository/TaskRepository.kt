package com.example.amanotes.data.repository

import com.example.amanotes.data.local.TaskDao
import com.example.amanotes.data.local.TaskEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {
    fun getAllTasks(): Flow<List<TaskEntity>> = taskDao.getAllTasks()
    
    fun getTasksByCompletion(isCompleted: Boolean): Flow<List<TaskEntity>> = 
        taskDao.getTasksByCompletion(isCompleted)
    
    suspend fun insertTask(title: String): Long {
        val task = TaskEntity(title = title)
        return taskDao.insertTask(task)
    }
    
    suspend fun updateTask(task: TaskEntity) {
        taskDao.updateTask(task)
    }
    
    suspend fun deleteTask(task: TaskEntity) {
        taskDao.deleteTask(task)
    }
    
    suspend fun toggleTaskCompletion(task: TaskEntity) {
        val updatedTask = task.copy(
            isCompleted = !task.isCompleted,
            updatedAt = System.currentTimeMillis()
        )
        taskDao.updateTask(updatedTask)
    }
    
    suspend fun deleteCompletedTasks() {
        taskDao.deleteCompletedTasks()
    }
}
