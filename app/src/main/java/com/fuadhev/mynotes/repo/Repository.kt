package com.fuadhev.mynotes.repo

import android.content.Context
import androidx.lifecycle.LiveData
import com.fuadhev.mynotes.entity.Note
import com.fuadhev.mynotes.entity.ToDo
import com.fuadhev.mynotes.room.MyRoomDatabase

class Repository(private val context:Context){

    private val db=MyRoomDatabase.getInstance(context)
    private val noteDb=db.notesDao()
    private val toDoDb=db.toDosDao()

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