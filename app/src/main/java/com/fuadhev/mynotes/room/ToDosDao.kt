package com.fuadhev.mynotes.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.fuadhev.mynotes.entity.Note
import com.fuadhev.mynotes.entity.ToDo


@Dao
interface ToDosDao {

    @Query("SELECT * FROM ToDo")
    fun getAllTodos(): LiveData<List<ToDo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: ToDo)

    @Update
    suspend fun updateTodo(todo: ToDo)


    @Delete
    suspend fun deleteTodo(todo: ToDo)
}