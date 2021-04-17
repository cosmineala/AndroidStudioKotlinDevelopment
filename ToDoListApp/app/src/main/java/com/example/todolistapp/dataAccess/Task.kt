package com.example.todolistapp.dataAccess

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity( tableName = "Tasks" )
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int,
    var text: String,
    var isDone: Boolean,
    var position: Int = Int.MAX_VALUE
)