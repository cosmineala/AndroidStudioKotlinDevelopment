package com.example.todolistapp.dataAccess

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

class TasksViewModel( application: Application ): AndroidViewModel(application) {

    val getAllLive: LiveData<List<Task>>
    private val repository: TasksRepository

    init {
        val tasksDao = TasksDatabase.GetDatabase( application ).TasksDao()
        repository = TasksRepository( tasksDao )
        getAllLive = repository.getAllLive
    }

    fun AddTask( task: Task ){
        viewModelScope.launch(Dispatchers.IO){
            repository.AddTask( task )
        }
    }
    fun AddTask( tasks: List<Task> ){
        viewModelScope.launch(Dispatchers.IO){
            repository.AddTask( tasks )
        }
    }

    fun UpdateTask(task: Task){
        viewModelScope.launch(Dispatchers.IO){
            repository.UpadateTask( task )
        }
    }
    fun UpdateTask( tasks: List<Task> ){
        viewModelScope.launch(Dispatchers.IO){
            repository.UpadateTask( tasks )
        }
    }


    fun DelTask( task: Task ){
        viewModelScope.launch(Dispatchers.IO){
            repository.DelTask( task )
        }
    }
    fun DelAll(){
        viewModelScope.launch(Dispatchers.IO){
            repository.DelAll()
        }
    }
    fun DelAllDone(){
        viewModelScope.launch(Dispatchers.IO){
            repository.DelAllDone()
        }
    }

    fun GetDoneCount(): Int{

        var count = 0
        runBlocking {
            count = repository.GetDoneCount()
        }
        return count
    }

    fun getNumberOfItems(): Int{
        var number = 0
        runBlocking{
            number = repository.getNumberOfItems()
        }
        return number
    }



}