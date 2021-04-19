package com.example.todolistapp.listFragment

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistapp.R
import com.example.todolistapp.dataAccess.Task
import com.example.todolistapp.dataAccess.TasksViewModel
import kotlinx.android.synthetic.main.item_todo.view.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TasksAdapter( listFragment: ListFragment, recyclerView: RecyclerView ):  RecyclerView.Adapter<TasksAdapter.MyViewHolder>() {

    private var tasksList = emptyList<Task>()

    var ignoreUpdates = 0

    val ignoreUpdatesLock = object{}

    val tasksViewModel = ViewModelProvider(listFragment).get( TasksViewModel::class.java )

    private val listFragment: ListFragment

        val simpleItemTouchCallback = createTouchCallback()

    init {
        this.listFragment = listFragment

        ItemTouchHelper(simpleItemTouchCallback)
                .attachToRecyclerView( recyclerView )
    }

    class MyViewHolder( itemView: View ): RecyclerView.ViewHolder(itemView){
    }


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

        if( ignoreUpdates == 0 )
        {
            this.tasksList = tasksList
            notifyDataSetChanged()
        }
        else
        {
            synchronized( ignoreUpdatesLock ){
                ignoreUpdates--
            }
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

                    listFragment.lifecycleScope.launch {

                        synchronized(ignoreUpdatesLock){
                            ignoreUpdates++
                        }

                        val tasks = listOf(selectedTask, reactTask) as List<Task>
                        tasksViewModel.UpdateTask(tasks)
                    }
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
                        }

                    }

                }

            }



    }



}