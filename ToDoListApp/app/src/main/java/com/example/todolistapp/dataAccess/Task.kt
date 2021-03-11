package com.example.todolistapp.dataAccess

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity( tableName = "Tasks" )
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val text: String,
    val isDone: Boolean
)