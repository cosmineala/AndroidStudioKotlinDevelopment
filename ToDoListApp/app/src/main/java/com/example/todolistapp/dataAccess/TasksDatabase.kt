package com.example.todolistapp.dataAccess

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database( entities = [Task::class], version = 1, exportSchema = false )
abstract class TasksDatabase: RoomDatabase() {

    abstract fun  TasksDao(): TasksDao

    companion object {

        @Volatile
        private var INSTANCE: TasksDatabase? = null

        fun GetDatabase( context: Context ): TasksDatabase{

            synchronized( this )
            {
                if ( INSTANCE != null)
                {
                    return INSTANCE as TasksDatabase
                }
                else
                {
                    val INSTANCE = Room.databaseBuilder( context.applicationContext, TasksDatabase::class.java, "Tasks_database")
                            .build()

                    return INSTANCE
                }
            }

        }

    }

}