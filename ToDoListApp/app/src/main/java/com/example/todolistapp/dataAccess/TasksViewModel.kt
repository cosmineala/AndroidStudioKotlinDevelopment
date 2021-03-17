package com.example.todolistapp.dataAccess

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

class TasksViewModel( application: Application ): AndroidViewModel(application) {

    val getAll: LiveData<List<Task>>
    private val repository: TasksRepository

    init {
        val tasksDao = TasksDatabase.GetDatabase( application ).TasksDao()
        repository = TasksRepository( tasksDao )
        getAll = repository.readAllData
    }

    fun AddTask( task: Task ){
        viewModelScope.launch(Dispatchers.IO){
            repository.AddTask( task )
        }
    }

    fun UpdateTask( task: Task ){
        viewModelScope.launch(Dispatchers.IO){
            repository.UpadateTask( task )
        }
    }

    fun DelTask( task: Task ){
        viewModelScope.launch(Dispatchers.IO){
            repository.DelTask( task )
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

}