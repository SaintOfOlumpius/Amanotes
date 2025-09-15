package com.example.amanotes.data.repository

import com.example.amanotes.data.local.NoteDao
import com.example.amanotes.data.local.NoteEntity
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDao: NoteDao) {
    fun getAllNotes(): Flow<List<NoteEntity>> = noteDao.getAllNotes()
    
    fun getNotesByCategory(category: String): Flow<List<NoteEntity>> = 
        noteDao.getNotesByCategory(category)
    
    fun getFavoriteNotes(): Flow<List<NoteEntity>> = noteDao.getFavoriteNotes()
    
    fun getAllCategories(): Flow<List<String>> = noteDao.getAllCategories()
    
    fun searchNotes(query: String): Flow<List<NoteEntity>> = noteDao.searchNotes(query)
    
    suspend fun insertNote(title: String, content: String, category: String = "General"): Long {
        val note = NoteEntity(
            title = title,
            content = content,
            category = category
        )
        return noteDao.insertNote(note)
    }
    
    suspend fun updateNote(note: NoteEntity) {
        val updatedNote = note.copy(updatedAt = System.currentTimeMillis())
        noteDao.updateNote(updatedNote)
    }
    
    suspend fun deleteNote(note: NoteEntity) {
        noteDao.deleteNote(note)
    }
    
    suspend fun toggleFavorite(note: NoteEntity) {
        noteDao.updateFavoriteStatus(note.id, !note.isFavorite)
    }
    
    suspend fun deleteNotesByCategory(category: String) {
        noteDao.deleteNotesByCategory(category)
    }
}
