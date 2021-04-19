package com.example.todolistapp.listFragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.todolistapp.dataAccess.TasksViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolistapp.R
import com.example.todolistapp.dataAccess.Task
import com.google.android.material.snackbar.*
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_list.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ListFragment : Fragment() {

    lateinit var tasksViewModel: TasksViewModel
    lateinit var thisView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        thisView = inflater.inflate(R.layout.fragment_list, container, false)

        val recyclerView = thisView.rvTasks
        val adapter = TasksAdapter( this, recyclerView )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        tasksViewModel = ViewModelProvider(this).get( TasksViewModel::class.java )

        tasksViewModel.getAllLive.observe( viewLifecycleOwner, Observer
        {
            adapter.setData( it )
        } )

        thisView.btAddToToDo.setOnClickListener {

            val text = thisView.etToDoInput.text.toString()

            if ( text.isNotEmpty() ){
                val task = Task( text = text , isDone = false)
                tasksViewModel.AddTask( task )
                etToDoInput.text.clear()
            }else{
               toast("Input is required")
            }
        }

        thisView.btDeleteSelected.setOnClickListener {

            val isDoneCount = tasksViewModel.GetDoneCount()

            if ( isDoneCount > 0 )
            {
                deleteDoneTasks( isDoneCount )
            }
            else
            {
                deleteAllTasks()
            }
        }

        thisView.etToDoInput.setOnClickListener {
            lifecycleScope.launch( Dispatchers.Main ){
                delay(500L)
                adapter.smoothScrollToLast()
            }
        }

        return thisView
    }

    private fun deleteDoneTasks(isDoneCount: Int ){

        AlertDialog.Builder( activity )
                .setTitle("Del done tasks Confirmation")
                .setMessage("You want to delet ${isDoneCount} done tasks ?")
                .setPositiveButton( "Confirm") { _, action ->
                    tasksViewModel.DelAllDone()

                }
                .setNegativeButton("Clancel") { _, action ->
                    toast("Del canceled")
                }
                .create()
                .show()
    }

    private fun deleteAllTasks(){

        val items = tasksViewModel.getNumberOfItems()

        if ( items == 0 )
        {
            toast("No tasks available to Del")
        }
        else
        {
            AlertDialog.Builder(activity)
                    .setTitle("Del all tasks Confirmation")
                    .setMessage("You want to delete all ${items} tasks ?")
                    .setPositiveButton("Confirm") { _, action ->
                        tasksViewModel.DelAll()

                    }
                    .setNegativeButton("Clancel") { _, action ->
                        toast("Del canceled")
                    }
                    .create()
                    .show()
        }
    }

    private fun snackbar( message: String ) {
      Snackbar.make( thisView, message, Snackbar.LENGTH_SHORT)
              .setAnimationMode( Snackbar.ANIMATION_MODE_SLIDE )
              .show()
    }

    private fun toast(message: String ){
        Toast.makeText( requireContext(), message, Toast.LENGTH_SHORT )
                .show()
    }



}