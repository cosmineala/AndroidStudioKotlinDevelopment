package com.example.todolistapp.dataAccess

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TasksDao {

    @Query("SELECT * FROM Tasks ORDER BY position ASC")
    fun GetAllLive(): LiveData<List<Task>>
    @Query("SELECT * FROM Tasks ORDER BY position ASC")
    fun getAll(): List<Task>

    @Insert
    suspend fun Add( task: Task )
    @Insert
    suspend fun Add( tasks: List<Task> )

    @Update
    suspend fun Update( task: Task )
    @Update
    suspend fun Update( tasks: List<Task> )

    @Query( "DELETE FROM Tasks WHERE isDone = 1" )
    suspend fun DelAllDone()
    @Delete
    suspend fun Del( tasks: Task )
    @Query( "DELETE FROM Tasks" )
    suspend fun DelAll()

    @Query("SELECT COUNT( * ) FROM Tasks WHERE isDone = 1")
    suspend fun GetDoneCount(): Int

    @Query( "SELECT COUNT( * ) FROM Tasks" )
    suspend fun getNumberOfItems() :Int

    @Query("SELECT * FROM Tasks WHERE isDone = 1")
    suspend fun GetAllDone(): List<Task>

    @Query("SELECT MAX(position) FROM Tasks")
    suspend fun GetLastPosition(): Int


}