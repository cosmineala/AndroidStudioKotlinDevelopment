package com.example.todolistapp.listFragment

import android.graphics.Color
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistapp.R
import com.example.todolistapp.dataAccess.Task
import kotlinx.android.synthetic.main.item_todo.view.*

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

            // NOTE Ne need to desable Listener to no trigger accidental Check box confirmation on other items
            cbIsDone.setOnCheckedChangeListener(null)

            // NOTE Bind the data and aplay the visual efects
            tvToDoTitle.text = currentItem.text
            cbIsDone.isChecked = currentItem.isDone
            toggleStrikeThrough( tvToDoTitle, currentItem.isDone )

            // NOTE Set Listener so the state can be updated
            cbIsDone.setOnCheckedChangeListener{ _, isChecked ->
                currentItem.isDone = isChecked
                listFragment.tasksViewModel.UpdateTask( currentItem )
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