package com.fuadhev.mynotes.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.fuadhev.mynotes.entity.Note

@Dao
interface NotesDao {

    @Query("SELECT * FROM Note")
    fun getAllNotes(): LiveData<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Query("SELECT * FROM Note WHERE note_title LIKE '%' || :title || '%'")
    fun getNotesByTitle(title: String): List<Note>

    @Delete
    suspend fun deleteNote(note: Note)
}