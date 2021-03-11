package com.example.todolistapp.dataAccess

import androidx.lifecycle.LiveData

class TasksRepository( private val tasksDao: TasksDao) {

    val readAllData: LiveData<List<Task>> = tasksDao.GetAll()

    suspend fun AddTask( task: Task ){
        tasksDao.Add( task )
    }
}