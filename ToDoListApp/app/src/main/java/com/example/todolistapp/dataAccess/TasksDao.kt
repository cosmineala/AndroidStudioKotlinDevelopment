package com.example.todolistapp.dataAccess

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TasksDao {

    @Query("SELECT * FROM Tasks ORDER BY id ASC")
    fun GetAll(): LiveData<List<Task>>

    @Insert
    suspend fun Add( task: Task )

    @Update
    suspend fun Update( task: Task )

    //    @Insert
//    fun AddAll( vararg tasks: Task )
//
//
//
//    @Delete
//    suspend fun Del( tasks: Task )
//    @Delete
//    fun DelAll( vararg tasks: Task )

}