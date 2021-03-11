package com.example.todolistapp

import android.graphics.Color
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_todo.view.*

class Adapter_ToDo( private val listToDo: MutableList<Model_ToDo>) : RecyclerView.Adapter< Adapter_ToDo.ViewHolder_ToDo >() {

    class  ViewHolder_ToDo( itemView: View ) : RecyclerView.ViewHolder( itemView )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder_ToDo {
        return  ViewHolder_ToDo(
            LayoutInflater.from( parent.context ).inflate(
                    R.layout.item_todo,
                    parent,
                    false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder_ToDo, position: Int) {
        val curent_Model_ToDo = listToDo[ position ]
        holder.itemView.apply {
            tvToDoTitle.text = curent_Model_ToDo.title
            cbIsDone.isChecked = curent_Model_ToDo.isChecked

            toggleStrikeThrough( tvToDoTitle, curent_Model_ToDo.isChecked )

            cbIsDone.setOnCheckedChangeListener { _, isChecked ->
                toggleStrikeThrough( tvToDoTitle, isChecked )
                curent_Model_ToDo.isChecked = !curent_Model_ToDo.isChecked
            }
        }
    }

    override fun getItemCount(): Int {
        return  listToDo.size
    }

    private fun toggleStrikeThrough( tvToDoTitle: TextView, isChecked: Boolean ){
        if( isChecked ) {
            tvToDoTitle.setTextColor( Color.RED )
            tvToDoTitle.paintFlags = tvToDoTitle.paintFlags or STRIKE_THRU_TEXT_FLAG
        } else {
            tvToDoTitle.paintFlags = tvToDoTitle.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
            tvToDoTitle.setTextColor( Color.DKGRAY )
        }
    }

    fun AddToDo( toDo: Model_ToDo ){
        listToDo.add( toDo )
        notifyItemInserted( listToDo.size - 1 )
    }

    fun  DelToDo(){
        listToDo.removeAll{ toDo ->
            toDo.isChecked
        }
        notifyDataSetChanged()
    }

    fun AreItemsToRemove() : Boolean {
        listToDo.forEach { item ->
            if ( item.isChecked ){
                return true
            }
        }
        return false
    }


}