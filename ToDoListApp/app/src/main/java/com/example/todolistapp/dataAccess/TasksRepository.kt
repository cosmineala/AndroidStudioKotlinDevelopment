package com.example.todolistapp.dataAccess

import androidx.lifecycle.LiveData
import java.lang.Exception

class TasksRepository( private val tasksDao: TasksDao) {

    val readAllData: LiveData<List<Task>> = tasksDao.GetAll()

    suspend fun AddTask( task: Task ){

        var position = 0

        try {
            position = tasksDao.GetLastPosition() + 1
        }catch ( e: Exception ){}

        task.position = position
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

    suspend fun SwapTasks( task1: Task, task2: Task ){

        val order1 = task1.position
        val order2 = task2.position

        task1.position = order2
        task2.position = order1

        tasksDao.Update( task1 )
        tasksDao.Update( task2 )
    }

}