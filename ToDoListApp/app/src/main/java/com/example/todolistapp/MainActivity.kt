package com.example.todolistapp

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter_ToDo: Adapter_ToDo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) // TODO -> Disables screen Totation

        adapter_ToDo = Adapter_ToDo( mutableListOf() )
        rvToDoItems.adapter = adapter_ToDo
        rvToDoItems.layoutManager = LinearLayoutManager( this )

        btAddToToDo.setOnClickListener {
            val toDoTitle = etToDoInput.text.toString()
            if ( toDoTitle.isNotEmpty() ){
                val toDo = Model_ToDo( toDoTitle )
                adapter_ToDo.AddToDo( toDo )
                etToDoInput.text.clear()
            }else{
                Toast.makeText(this,"No data entered",Toast.LENGTH_SHORT).show()
            }
        }

        btDeleteSelected.setOnClickListener {
            if ( adapter_ToDo.AreItemsToRemove() ) {
                adapter_ToDo.DelToDo()
            }else{
                Toast.makeText(this,"No item selected",Toast.LENGTH_SHORT).show()
            }
        }



    }
}