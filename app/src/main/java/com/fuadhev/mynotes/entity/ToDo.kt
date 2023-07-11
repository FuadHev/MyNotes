package com.fuadhev.mynotes.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ToDo")
data class ToDo(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "todo_txt") val todo_txt:String,
//    @ColumnInfo(name = "day") val day: Int,
//    @ColumnInfo(name = "month") val month: Int,
//    @ColumnInfo(name = "clock") val clock: String,
    @ColumnInfo(name = "isComplete") var isComplete:Boolean) {
}