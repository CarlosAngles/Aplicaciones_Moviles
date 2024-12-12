package com.example.appbd

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TaskDashboardActivity : AppCompatActivity() {

    private lateinit var taskRecyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_dashboard)

        // Inicializa Firebase
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Inicializa la vista del RecyclerView
        taskRecyclerView = findViewById(R.id.recyclerViewTasks)
        taskRecyclerView.layoutManager = LinearLayoutManager(this)

        // Configura el adaptador
        taskAdapter = TaskAdapter(
            taskList = emptyList(), // Inicia el adaptador con una lista vacía
            onEditClickListener = { position -> onEditTask(position) },
            onDeleteClickListener = { position -> onDeleteTask(position) }
        )
        taskRecyclerView.adapter = taskAdapter

        // Configura el botón para agregar una tarea
        findViewById<Button>(R.id.addTaskButton).setOnClickListener {
            showAddTaskDialog()
        }

        // Configuración del botón para cerrar sesión
        findViewById<Button>(R.id.buttonLogout).setOnClickListener {
            logout()
        }

        loadTasks()
    }

    override fun onResume() {
        super.onResume()
        loadTasks()  // Vuelve a cargar las tareas cuando la actividad regresa al primer plano
    }

    private fun loadTasks() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            firestore.collection("tasks")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener { documents ->
                    val tasks = mutableListOf<Task>()
                    for (document in documents) {
                        val task = document.toObject(Task::class.java)
                        val taskId = document.id  // Obtén el taskId (ID del documento en Firestore)
                        tasks.add(task.copy(taskId = taskId)) // Asigna el taskId
                    }
                    taskAdapter.updateTaskList(tasks) // Actualiza el adaptador con las nuevas tareas
                    Log.d("TaskDashboard", "Tareas cargadas: ${tasks.size}")
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Error al cargar tareas: ${exception.message}", Toast.LENGTH_SHORT).show()
                    Log.e("TaskDashboard", "Error al cargar tareas", exception)
                }
        }
    }

    private fun showAddTaskDialog() {
        // Crear vista personalizada para el AlertDialog
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null)
        val titleEditText: EditText = dialogView.findViewById(R.id.editTextTaskTitle)
        val descriptionEditText: EditText = dialogView.findViewById(R.id.editTextTaskDescription)

        // Crear el AlertDialog
        AlertDialog.Builder(this)
            .setTitle("Agregar Tarea")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val title = titleEditText.text.toString()
                val description = descriptionEditText.text.toString()

                if (title.isEmpty() || description.isEmpty()) {
                    Toast.makeText(this, "Por favor ingresa un título y una descripción", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val userId = auth.currentUser?.uid ?: return@setPositiveButton

                // Crear la nueva tarea
                val task = Task(title = title, description = description, userId = userId)

                // Guardar en Firestore
                firestore.collection("tasks")
                    .add(task)
                    .addOnSuccessListener { documentReference ->
                        val taskId = documentReference.id  // Obtén el ID generado por Firestore
                        // Actualiza el task con el taskId
                        val updatedTask = task.copy(taskId = taskId)

                        // Guarda el task con el taskId actualizado
                        firestore.collection("tasks")
                            .document(taskId)
                            .set(updatedTask)  // Usa set() para guardar los datos con el taskId

                        Toast.makeText(this, "Tarea agregada", Toast.LENGTH_SHORT).show()
                        loadTasks()  // Recargar las tareas
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al guardar la tarea", Toast.LENGTH_SHORT).show()
                    }

            }
            .setNegativeButton("Cancelar", null)
            .create()
            .show()
    }

    private fun onEditTask(position: Int) {
        val task = taskAdapter.taskListPublic[position]

        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_task, null)
        val titleEditText: EditText = dialogView.findViewById(R.id.editTextTaskTitle)
        val descriptionEditText: EditText = dialogView.findViewById(R.id.editTextTaskDescription)

        titleEditText.setText(task.title)
        descriptionEditText.setText(task.description)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Editar Tarea")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val updatedTitle = titleEditText.text.toString()
                val updatedDescription = descriptionEditText.text.toString()

                // Usar taskId para actualizar la tarea en Firestore
                val taskUpdates: Map<String, Any> = hashMapOf(
                    "title" to updatedTitle,
                    "description" to updatedDescription
                )

                firestore.collection("tasks")
                    .document(task.taskId)  // Usar taskId para referencia del documento
                    .update(taskUpdates)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Tarea actualizada", Toast.LENGTH_SHORT).show()
                        loadTasks()  // Recargar las tareas
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this, "Error al actualizar tarea: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("Cancelar", null)
            .create()
            .show()
    }

    private fun onDeleteTask(position: Int) {
        val task = taskAdapter.taskListPublic[position]

        // Confirmar eliminación de tarea
        AlertDialog.Builder(this)
            .setMessage("¿Seguro que deseas eliminar esta tarea?")
            .setPositiveButton("Eliminar") { _, _ ->
                firestore.collection("tasks")
                    .document(task.taskId)  // Usar taskId para referencia del documento
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Tarea eliminada", Toast.LENGTH_SHORT).show()
                        loadTasks()  // Recargar las tareas
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this, "Error al eliminar tarea: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun logout() {
        auth.signOut()
        Toast.makeText(this, "Has cerrado sesión", Toast.LENGTH_SHORT).show()
        // Redirige al MainActivity después de cerrar sesión
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}