package com.example.todolistapp.listFragment

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistapp.R
import com.example.todolistapp.dataAccess.Task
import com.example.todolistapp.dataAccess.TasksViewModel
import kotlinx.android.synthetic.main.item_todo.view.*

class TasksAdapter( listFragment: ListFragment, val recyclerView: RecyclerView ):  RecyclerView.Adapter<TasksAdapter.TaskItemViewHolder>() {

    private var tasksList = emptyList<Task>()
    private val tasksViewModel = ViewModelProvider(listFragment).get( TasksViewModel::class.java )
    private val updateManager = UpdateManager()

    init {
        ItemTouchHelper(createTouchCallback())
                .attachToRecyclerView( recyclerView )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskItemViewHolder{

        val viewHolder = TaskItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false))
        return viewHolder
    }

    override fun onBindViewHolder(holder: TaskItemViewHolder, position: Int) {

        val currentItem = tasksList[position]

        holder.itemView.apply{

            cbIsDone.setOnCheckedChangeListener(null)

            tvToDoTitle.text = currentItem.text
            cbIsDone.isChecked = currentItem.isDone
            toggleStrikeThrough( tvToDoTitle, currentItem.isDone )

            cbIsDone.setOnCheckedChangeListener{ _, isChecked ->
                currentItem.isDone = isChecked
                tasksViewModel.UpdateTask( currentItem )
            }

        }
    }

    override fun getItemCount(): Int {
        return tasksList.size
    }

    fun setData( tasksList: List<Task> ){
        if ( updateManager.canUpdate() )
        {
            this.tasksList = tasksList
            notifyDataSetChanged()
        }
    }

    fun smoothScrollToLast(){
        if ( tasksList.size > 0 )
        {
            recyclerView.smoothScrollToPosition(tasksList.size - 1)
        }
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

    class TaskItemViewHolder(itemView: View ): RecyclerView.ViewHolder(itemView){}

    private fun createTouchCallback(): SimpleCallback

        = object : ItemTouchHelper.SimpleCallback(UP or DOWN or START or END, 0) {

            override fun onMove( recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {

                val from = viewHolder.adapterPosition
                val to = target.adapterPosition

                val selectedTask = tasksList.find { it.position == from }
                val reactTask =  tasksList.find { it.position == to }

                if ( selectedTask != null && reactTask != null )
                {
                    Task.swapPossitions(selectedTask, reactTask)
                    notifyItemMoved(to, from)
                }
                return true
            }

        override fun onSwiped( viewHolder: RecyclerView.ViewHolder, direction: Int ) {}

        override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

                when( actionState ){

                    ACTION_STATE_DRAG ->
                    {
                        if ( isCurrentlyActive )
                        {
                            viewHolder.itemView.setBackgroundColor(Color.GRAY)
                        }
                        else
                        {
                            viewHolder.itemView.setBackgroundColor(Color.WHITE)

                            updateManager.ignore(1)
                            tasksViewModel.UpdateTask( tasksList )
                        }

                    }

                }
            }
    }

    class UpdateManager(){
        private var number = 0
        private val lock = object{}

        fun ignore( number: Int ){
            synchronized( lock ){
                this.number += number
            }
        }

        fun canUpdate(): Boolean{
            synchronized(lock){
                if ( number > 0 )
                {
                    number--
                    return false
                }
                else
                {
                    return true
                }
            }
        }
    }



}