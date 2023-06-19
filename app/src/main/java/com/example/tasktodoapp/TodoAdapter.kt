package com.example.tasktodoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class TodoAdapter(val list: List<TodoModel>) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(itemView)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(list[position])
    }

    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val viewColorTag: View = itemView.findViewById(R.id.viewColorTag)
        private val category: TextView = itemView.findViewById(R.id.category)
        private val taskSubtitle: TextView = itemView.findViewById(R.id.taskSubtitle)
        private val taskTitle: TextView = itemView.findViewById(R.id.taskTitle)
        private val timeNum: TextView = itemView.findViewById(R.id.timeNum)
        private val dateNum: TextView = itemView.findViewById(R.id.dateNum)

        fun bind(todoModel: TodoModel) {
            val colors = itemView.resources.getIntArray(R.array.random_color)
            val randomColor = colors.random()
            viewColorTag.setBackgroundColor(randomColor)

            taskTitle.text = todoModel.title
            taskSubtitle.text = todoModel.description
            category.text = todoModel.category

            updateTime(todoModel.time)
            updateDate(todoModel.date)
        }

        private fun updateTime(time: Long) {
            val myformat = "h:mm a"
            val sdf = SimpleDateFormat(myformat, Locale.getDefault())
            timeNum.text = sdf.format(Date(time))
        }

        private fun updateDate(date: Long) {
            val myformat = "EEE, d MMM yyyy"
            val sdf = SimpleDateFormat(myformat, Locale.getDefault())
            dateNum.text = sdf.format(Date(date))
        }
    }
}
