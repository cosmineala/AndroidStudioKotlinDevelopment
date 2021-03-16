package com.example.todolistapp.listFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.todolistapp.dataAccess.TasksViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolistapp.R
import com.example.todolistapp.dataAccess.Task
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_list.view.*
import kotlinx.android.synthetic.main.item_todo.*
import kotlinx.android.synthetic.main.item_todo.view.*


class ListFragment : Fragment() {

    lateinit var tasksViewModel: TasksViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        val adapter = TasksAdapter( this )
        val recyclerView = view.rvTasks
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        tasksViewModel = ViewModelProvider(this).get( TasksViewModel::class.java )
        tasksViewModel.getAll.observe( viewLifecycleOwner, Observer { task ->
            adapter.setData( task )
        } )


        view.btAddToToDo.setOnClickListener {

           // val text = view.etToDoInput.text.toString()
            val task = Task( 0, view.etToDoInput.text.toString() , false)
            tasksViewModel.AddTask( task )
            etToDoInput.text.clear()

        }

        view.btDeleteSelected.setOnClickListener {
            val list = tasksViewModel.getAll

            list.value?.forEach { value ->
                if ( value.isDone ){
                    tasksViewModel.DelTask(value)
                }
            }
        }



        return view
    }


}