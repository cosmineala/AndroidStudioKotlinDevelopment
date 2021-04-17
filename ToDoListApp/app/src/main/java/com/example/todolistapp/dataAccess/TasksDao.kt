package com.example.todolistapp.dataAccess

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TasksDao {

    @Query("SELECT * FROM Tasks ORDER BY position ASC")
    fun GetAll(): LiveData<List<Task>>

    @Insert
    suspend fun Add( task: Task )

    @Update
    suspend fun Update( task: Task )

    @Query( "DELETE FROM Tasks WHERE isDone = 1" )
    suspend fun DelAllDone()

    @Query("SELECT COUNT( * ) FROM Tasks WHERE isDone = 1")
    suspend fun GetDoneCount(): Int

    @Query("SELECT * FROM Tasks WHERE isDone = 1")
    suspend fun GetAllDone(): List<Task>

    @Delete
    suspend fun Del( tasks: Task )

    @Query("SELECT MAX(position) from TASKS")
    suspend fun GetLastPosition(): Int



}