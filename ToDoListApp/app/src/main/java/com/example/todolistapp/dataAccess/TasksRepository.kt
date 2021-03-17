package com.example.todolistapp.dataAccess

import androidx.lifecycle.LiveData

class TasksRepository( private val tasksDao: TasksDao) {

    val readAllData: LiveData<List<Task>> = tasksDao.GetAll()

    suspend fun AddTask( task: Task ){
        tasksDao.Add( task )
    }

    suspend fun UpadateTask( task: Task ){
        tasksDao.Update(task)
    }

    suspend fun DelTask( task: Task ){
        tasksDao.Del(task)
    }

    suspend fun DelAllDone(){
        tasksDao.DelAllDone()
    }

    suspend fun GetDoneCount(): Int{
        return tasksDao.GetDoneCount()
    }

}