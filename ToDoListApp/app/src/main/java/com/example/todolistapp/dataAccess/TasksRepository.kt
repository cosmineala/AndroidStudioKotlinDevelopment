package com.example.todolistapp.dataAccess

import androidx.lifecycle.LiveData
import java.lang.Exception

class TasksRepository( private val tasksDao: TasksDao) {

    val getAllLive: LiveData<List<Task>> = tasksDao.GetAllLive()

    suspend fun AddTask( task: Task ){

        var position = 0
        try
        {
            position = tasksDao.GetLastPosition() + 1
        }
        catch ( e: Exception ){}

        task.position = position
        tasksDao.Add( task )
    }
    suspend fun AddTask( tasks: List<Task> ){
        tasks.forEach {
            AddTask( it )
        }
    }

    suspend fun UpadateTask( task: Task ){
        tasksDao.Update(task)
    }
    suspend fun UpadateTask( tasks: List<Task> ){
        tasksDao.Update(tasks)
    }

    suspend fun DelTask( task: Task ){
        tasksDao.Del(task)
        RepairOrder()
    }
    suspend fun DelAllDone(){
        tasksDao.DelAllDone()
        RepairOrder()
    }
    suspend fun DelAll(){
        tasksDao.DelAll()
        RepairOrder()
    }

    suspend fun GetDoneCount(): Int{
        return tasksDao.GetDoneCount()
    }

    suspend fun getNumberOfItems() = tasksDao.getNumberOfItems()


    suspend fun RepairOrder(){
        val tasks = tasksDao.getAll()
        var position = 0

        tasks.forEach {
            it.position = position++
        }
        tasksDao.Update( tasks )
    }


    suspend fun GetTaskByPosition( position: Int ): Task{

        var list: List<Task>? = tasksDao.GetAllLive().value

        if (list != null) {
            list.forEach {
                if ( it.position == position ){
                    return it
                }
            }
        }

        return Task( 0,"NUAMERS", false, position )
    }



}