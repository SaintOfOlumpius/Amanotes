package com.example.amanotes.data.repository

import com.example.amanotes.data.firestore.FirestoreManager
import com.example.amanotes.data.firestore.Note
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class NoteRepositoryFirestore {
    
    private val firestore = FirestoreManager.getFirestore()
    private val collection = firestore.collection("notes")
    
    fun getAllNotes(userId: String): Flow<List<Note>> = callbackFlow {
        val listener = collection
            .whereEqualTo("userId", userId)
            .orderBy("updatedAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                val notes = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Note::class.java)?.apply {
                        id = doc.id
                    }
                } ?: emptyList()
                
                trySend(notes)
            }
        
        awaitClose { listener.remove() }
    }
    
    fun getNotesByCategory(userId: String, category: String): Flow<List<Note>> = callbackFlow {
        val listener = collection
            .whereEqualTo("userId", userId)
            .whereEqualTo("category", category)
            .orderBy("updatedAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                val notes = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Note::class.java)?.apply {
                        id = doc.id
                    }
                } ?: emptyList()
                
                trySend(notes)
            }
        
        awaitClose { listener.remove() }
    }
    
    fun getFavoriteNotes(userId: String): Flow<List<Note>> = callbackFlow {
        val listener = collection
            .whereEqualTo("userId", userId)
            .whereEqualTo("isFavorite", true)
            .orderBy("updatedAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                val notes = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Note::class.java)?.apply {
                        id = doc.id
                    }
                } ?: emptyList()
                
                trySend(notes)
            }
        
        awaitClose { listener.remove() }
    }
    
    fun getAllCategories(userId: String): Flow<List<String>> = callbackFlow {
        val listener = collection
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                val categories = snapshot?.documents
                    ?.mapNotNull { it.getString("category") }
                    ?.distinct()
                    ?.sorted()
                    ?: emptyList()
                
                trySend(categories)
            }
        
        awaitClose { listener.remove() }
    }
    
    fun searchNotes(userId: String, query: String): Flow<List<Note>> = callbackFlow {
        // Firestore doesn't support full-text search, so we filter client-side
        // For production, consider using Algolia or Cloud Firestore extensions
        val listener = collection
            .whereEqualTo("userId", userId)
            .orderBy("updatedAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                val notes = snapshot?.documents
                    ?.mapNotNull { doc ->
                        doc.toObject(Note::class.java)?.apply {
                            id = doc.id
                        }
                    }
                    ?.filter { note ->
                        note.title.contains(query, ignoreCase = true) ||
                        note.content.contains(query, ignoreCase = true)
                    } ?: emptyList()
                
                trySend(notes)
            }
        
        awaitClose { listener.remove() }
    }
    
    suspend fun insertNote(
        userId: String,
        title: String,
        content: String,
        category: String = "General"
    ): String {
        val note = Note(
            userId = userId,
            title = title,
            content = content,
            category = category,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        
        val docRef = collection.add(note).await()
        return docRef.id
    }
    
    suspend fun updateNote(note: Note) {
        val updatedNote = note.copy(updatedAt = System.currentTimeMillis())
        collection.document(note.id)
            .set(updatedNote)
            .await()
    }
    
    suspend fun deleteNote(noteId: String) {
        collection.document(noteId).delete().await()
    }
    
    suspend fun toggleFavorite(note: Note) {
        val updatedNote = note.copy(
            isFavorite = !note.isFavorite,
            updatedAt = System.currentTimeMillis()
        )
        collection.document(note.id)
            .set(updatedNote)
            .await()
    }
    
    suspend fun deleteNotesByCategory(userId: String, category: String) {
        val snapshot = collection
            .whereEqualTo("userId", userId)
            .whereEqualTo("category", category)
            .get()
            .await()
        
        snapshot.documents.forEach { doc ->
            doc.reference.delete().await()
        }
    }
    
    suspend fun getNoteById(id: String): Note? {
        return try {
            val doc = collection.document(id).get().await()
            doc.toObject(Note::class.java)?.apply {
                this.id = doc.id
            }
        } catch (e: Exception) {
            null
        }
    }
}

