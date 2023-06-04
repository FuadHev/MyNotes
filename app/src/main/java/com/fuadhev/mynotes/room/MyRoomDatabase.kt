package com.fuadhev.mynotes.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fuadhev.mynotes.entity.Note
import com.fuadhev.mynotes.entity.ToDo

@Database(entities = [Note::class, ToDo::class], version = 1)
abstract class MyRoomDatabase :RoomDatabase(){

    abstract fun notesDao(): NotesDao
    abstract fun toDosDao(): ToDosDao

    companion object{
        private var instance: MyRoomDatabase? = null

        fun getInstance(context: Context): MyRoomDatabase {
            return instance ?: synchronized(this) {
                val databaseBuilder = Room.databaseBuilder(
                    context.applicationContext,
                    MyRoomDatabase::class.java,
                    "note_database"
                )
                instance = databaseBuilder.build()
                instance!!
            }
        }
        }
    }
