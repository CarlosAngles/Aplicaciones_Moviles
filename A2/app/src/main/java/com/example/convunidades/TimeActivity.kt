package com.example.convunidades

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TimeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time)

        // Obtener los elementos de la interfaz
        val btnBack = findViewById<Button>(R.id.btn_back)
        val etQuantity = findViewById<EditText>(R.id.et_quantity)
        val spinnerFromUnit = findViewById<Spinner>(R.id.spinner_from_unit)
        val spinnerToUnit = findViewById<Spinner>(R.id.spinner_to_unit)
        val btnConvert = findViewById<Button>(R.id.btn_convert)
        val tvResult = findViewById<TextView>(R.id.tv_result)

        // Lista de unidades de tiempo
        val units = arrayOf(
            "Segundos", "Milisegundos (ms)", "Microsegundos (µs)", "Minutos", "Horas",
            "Días", "Semanas", "Meses", "Años"
        )

        // Configurar el Spinner para unidades de origen
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, units)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFromUnit.adapter = adapter

        // Configurar el Spinner para unidades de destino
        spinnerToUnit.adapter = adapter

        // Acción para retroceder
        btnBack.setOnClickListener {
            finish() // Regresa a la actividad anterior
        }

        // Acción para convertir
        btnConvert.setOnClickListener {
            val quantity = etQuantity.text.toString().toDoubleOrNull()
            if (quantity != null) {
                val fromUnit = spinnerFromUnit.selectedItem.toString()
                val toUnit = spinnerToUnit.selectedItem.toString()

                // Lógica de conversión
                val result = convertTime(quantity, fromUnit, toUnit)
                tvResult.text = "Resultado: $result $toUnit"
            } else {
                tvResult.text = "Por favor ingrese una cantidad válida"
            }
        }
    }

    // Función para convertir el tiempo
    private fun convertTime(quantity: Double, fromUnit: String, toUnit: String): Double {
        var result = quantity

        // Primero convertimos la cantidad a segundos
        result = when (fromUnit) {
            "Milisegundos (ms)" -> quantity / 1000
            "Microsegundos (µs)" -> quantity / 1000000
            "Minutos" -> quantity * 60
            "Horas" -> quantity * 3600
            "Días" -> quantity * 86400
            "Semanas" -> quantity * 604800
            "Meses" -> quantity * 2592000 // Aproximadamente 30 días por mes
            "Años" -> quantity * 31536000 // Aproximadamente 365 días por año
            else -> quantity // Si ya son segundos, no hacemos nada
        }

        // Ahora convertimos de segundos a la unidad de destino
        return when (toUnit) {
            "Milisegundos (ms)" -> result * 1000
            "Microsegundos (µs)" -> result * 1000000
            "Minutos" -> result / 60
            "Horas" -> result / 3600
            "Días" -> result / 86400
            "Semanas" -> result / 604800
            "Meses" -> result / 2592000
            "Años" -> result / 31536000
            else -> result // Si es segundos, ya está en la unidad correcta
        }
    }
}
