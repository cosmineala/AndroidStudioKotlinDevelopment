package com.example.todolistapp.dataAccess

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity( tableName = "Tasks" )
data class Task(
        @PrimaryKey(autoGenerate = true) var id: Int,
        var text: String,
        var isDone: Boolean,
        var position: Int = Int.MAX_VALUE
){

    companion object{

        fun swapPossitions( task1: Task, task2: Task ){
            val aux = task1.position
            task1.position = task2.position
            task2.position = aux
        }
    }
}