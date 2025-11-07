package com.example.amanotes.data.remote

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

object FirebaseStorageService {
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference
    
    suspend fun uploadFile(userId: String, fileName: String, uri: Uri): String {
        val fileRef = storageRef.child("users/$userId/$fileName")
        val uploadTask = fileRef.putFile(uri).await()
        return uploadTask.storage.downloadUrl.await().toString()
    }
    
    suspend fun uploadBytes(userId: String, fileName: String, bytes: ByteArray): String {
        val fileRef = storageRef.child("users/$userId/$fileName")
        val uploadTask = fileRef.putBytes(bytes).await()
        return uploadTask.storage.downloadUrl.await().toString()
    }
    
    suspend fun deleteFile(fileUrl: String) {
        try {
            val fileRef = storage.getReferenceFromUrl(fileUrl)
            fileRef.delete().await()
        } catch (e: Exception) {
            // File might not exist, ignore
        }
    }
    
    suspend fun getFileUrl(userId: String, fileName: String): String? {
        return try {
            val fileRef = storageRef.child("users/$userId/$fileName")
            fileRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            null
        }
    }
}

