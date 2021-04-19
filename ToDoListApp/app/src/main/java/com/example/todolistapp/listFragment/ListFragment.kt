package com.example.todolistapp.listFragment

import android.app.AlertDialog
import android.content.DialogInterface
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
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_list.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ListFragment : Fragment() {

    lateinit var tasksViewModel: TasksViewModel

    var delCount = 0
    val delCountLock = object{}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        val recyclerView = view.rvTasks
        val adapter = TasksAdapter( this, recyclerView )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        tasksViewModel = ViewModelProvider(this).get( TasksViewModel::class.java )

        tasksViewModel.getAllLive.observe( viewLifecycleOwner, Observer
        {
            adapter.setData( it )
        } )


        view.btAddToToDo.setOnClickListener {

           // val text = view.etToDoInput.text.toString()

            if ( view.etToDoInput.text.isNotEmpty() ){
                val task = Task( 0, view.etToDoInput.text.toString() , false)
                tasksViewModel.AddTask( task )
                etToDoInput.text.clear()
            }else{
                Toast.makeText( activity , "Input is recurred",Toast.LENGTH_SHORT ).show()
            }


        }


        view.btDeleteSelected.setOnClickListener {

            val isDoneCount = tasksViewModel.GetDoneCount()

            if ( isDoneCount > 0 )
            {
                deleteDoneTasks( isDoneCount)
            }
            else
            {
                deleteAllTasks()
            }


        }


        return view
    }

    fun deleteDoneTasks(isDoneCount: Int ){

        AlertDialog.Builder( activity )
                .setTitle("Del done tasks Confirmation")
                .setMessage("You want to delet ${isDoneCount} done tasks ?")
                .setPositiveButton( "Confirm",
                        DialogInterface.OnClickListener{ _, action ->
                            tasksViewModel.DelAllDone()

                        })
                .setNegativeButton("Clancel",
                        DialogInterface.OnClickListener{_, action ->
                            Toast.makeText( activity , "Del canceled",Toast.LENGTH_SHORT ).show()
                        })
                .create()
                .show()
    }

    fun deleteAllTasks(){

        val items = tasksViewModel.getNumberOfItems()

        if ( items == 0 )
        {
            Toast.makeText(activity, "No tasks available to Del", Toast.LENGTH_SHORT).show()
        }
        else
        {
            AlertDialog.Builder(activity)
                    .setTitle("Del all tasks Confirmation")
                    .setMessage("You want to delete all ${items} tasks ?")
                    .setPositiveButton("Confirm",
                            DialogInterface.OnClickListener { _, action ->
                                tasksViewModel.DelAll()

                            })
                    .setNegativeButton("Clancel",
                            DialogInterface.OnClickListener { _, action ->
                                Toast.makeText(activity, "Del canceled", Toast.LENGTH_SHORT).show()
                            })
                    .create()
                    .show()
        }
    }





}