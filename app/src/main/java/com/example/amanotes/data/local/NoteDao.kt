package com.example.amanotes.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY updatedAt DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE category = :category ORDER BY updatedAt DESC")
    fun getNotesByCategory(category: String): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE isFavorite = 1 ORDER BY updatedAt DESC")
    fun getFavoriteNotes(): Flow<List<NoteEntity>>

    @Query("SELECT DISTINCT category FROM notes ORDER BY category ASC")
    fun getAllCategories(): Flow<List<String>>

    @Query("SELECT * FROM notes WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' ORDER BY updatedAt DESC")
    fun searchNotes(query: String): Flow<List<NoteEntity>>

    @Insert
    suspend fun insertNote(note: NoteEntity): Long

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Delete
    suspend fun deleteNote(note: NoteEntity)

    @Query("UPDATE notes SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Long, isFavorite: Boolean)

    @Query("DELETE FROM notes WHERE category = :category")
    suspend fun deleteNotesByCategory(category: String)
}
