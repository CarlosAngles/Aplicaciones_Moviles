package com.example.appbd

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private var taskList: List<Task>, // Keep taskList private, but mutable
    private val onEditClickListener: (Int) -> Unit,
    private val onDeleteClickListener: (Int) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    // Getter method to expose taskList
    val taskListPublic: List<Task>
        get() = taskList

    // Se crea la vista de cada item del RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    // Asigna los datos de cada tarea a las vistas del ViewHolder
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.titleTextView.text = task.title
        holder.descriptionTextView.text = task.description

        // Configura el listener de edición
        holder.editButton.setOnClickListener {
            onEditClickListener(position) // Llama al listener de edición
        }

        // Configura el listener de eliminación
        holder.deleteButton.setOnClickListener {
            onDeleteClickListener(position) // Llama al listener de eliminación
        }
    }

    // Retorna el número de elementos de la lista
    override fun getItemCount(): Int = taskList.size

    // Método para actualizar la lista de tareas
    fun updateTaskList(newList: List<Task>) {
        taskList = newList
        notifyDataSetChanged()  // Notifica al RecyclerView que la lista ha cambiado
    }

    // ViewHolder que contiene las vistas de cada item
    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.textViewTaskTitle)
        val descriptionTextView: TextView = itemView.findViewById(R.id.textViewTaskDescription)
        val editButton: Button = itemView.findViewById(R.id.buttonEditTask)
        val deleteButton: Button = itemView.findViewById(R.id.buttonDeleteTask)
    }
}
