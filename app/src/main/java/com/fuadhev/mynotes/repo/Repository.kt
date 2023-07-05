package com.fuadhev.mynotes.repo

import android.content.Context
import androidx.lifecycle.LiveData
import com.fuadhev.mynotes.entity.Note
import com.fuadhev.mynotes.entity.ToDo
import com.fuadhev.mynotes.room.MyRoomDatabase
import com.fuadhev.mynotes.room.NotesDao
import com.fuadhev.mynotes.room.ToDosDao
import javax.inject.Inject

class Repository @Inject constructor(private val noteDb:NotesDao,private val toDoDb: ToDosDao){


    fun getAllNotes(): LiveData<List<Note>> {
        return noteDb.getAllNotes()
    }

    fun getAllToDos():LiveData<List<ToDo>>{
        return toDoDb.getAllTodos()
    }

    suspend fun insertNote(note: Note){
        return noteDb.insertNote(note)
    }

    suspend fun deleteNote(note:Note){
        return noteDb.deleteNote(note)
    }

}