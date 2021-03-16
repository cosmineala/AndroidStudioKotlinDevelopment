package com.example.todolistapp.listFragment

import android.app.Activity
import android.graphics.Color
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistapp.R
import com.example.todolistapp.dataAccess.Task
import com.example.todolistapp.dataAccess.TasksViewModel
import kotlinx.android.synthetic.main.item_todo.view.*
import java.util.EnumSet.of
import kotlin.coroutines.coroutineContext

class TasksAdapter( listFragment: ListFragment ):  RecyclerView.Adapter<TasksAdapter.MyViewHolder>() {

    private var tasksList = emptyList<Task>()
    private lateinit var listFragment: ListFragment

    init {
        this.listFragment = listFragment
    }

    class MyViewHolder( itemView: View ): RecyclerView.ViewHolder(itemView){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val viewHolder = MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false))

        return viewHolder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = tasksList[position]

        holder.itemView.apply {
            tvToDoTitle.text = currentItem.text
            cbIsDone.isChecked = currentItem.isDone

            toggleStrikeThrough( tvToDoTitle, currentItem.isDone )

            cbIsDone.setOnCheckedChangeListener{ _, isChecked ->

              // cbIsDone.isChecked = false

                listFragment.tasksViewModel.UpdateTask( Task( currentItem.id, currentItem.text, currentItem.isDone.not() )  )

            }
        }

    }

    override fun getItemCount(): Int {
        return tasksList.size
    }

    fun setData( tasksList: List<Task> ){
        this.tasksList = tasksList
        notifyDataSetChanged()
    }

    private fun toggleStrikeThrough(tvToDoTitle: TextView, isChecked: Boolean ){
        if( isChecked ) {
            tvToDoTitle.setTextColor( Color.RED )
            tvToDoTitle.paintFlags = tvToDoTitle.paintFlags or STRIKE_THRU_TEXT_FLAG
        } else {
            tvToDoTitle.paintFlags = tvToDoTitle.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
            tvToDoTitle.setTextColor( Color.DKGRAY )
        }
    }
}