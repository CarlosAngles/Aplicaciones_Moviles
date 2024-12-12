package com.example.convunidades

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SpeedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speed)

        // Ajustar los márgenes para diseño edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtener referencias de la UI
        val btnBack = findViewById<Button>(R.id.btn_back)
        val etQuantity = findViewById<EditText>(R.id.et_quantity)
        val spinnerFromUnit = findViewById<Spinner>(R.id.spinner_from_unit)
        val spinnerToUnit = findViewById<Spinner>(R.id.spinner_to_unit)
        val btnConvert = findViewById<Button>(R.id.btn_convert)
        val tvResult = findViewById<TextView>(R.id.tv_result)

        // Lista de unidades de velocidad
        val units = arrayOf("m/s", "km/h", "mi/h", "nudos", "km/s", "mm/s", "milla/s", "pie/s")

        // Configurar los spinners
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, units)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFromUnit.adapter = adapter
        spinnerToUnit.adapter = adapter
        btnBack.setOnClickListener {
            finish()// Regresa a la actividad anterior
        }
        // Acción del botón de conversión
        btnConvert.setOnClickListener {
            val quantity = etQuantity.text.toString().toDoubleOrNull()
            if (quantity != null) {
                val fromUnit = spinnerFromUnit.selectedItem.toString()
                val toUnit = spinnerToUnit.selectedItem.toString()

                // Lógica de conversión
                val result = convertSpeed(quantity, fromUnit, toUnit)
                tvResult.text = "Resultado: $result $toUnit"
            } else {
                tvResult.text = "Por favor ingrese una cantidad válida"
            }
        }
    }

    // Función para convertir velocidad
    private fun convertSpeed(quantity: Double, fromUnit: String, toUnit: String): Double {
        var result = quantity

        // Convertir a m/s (unidad base)
        result = when (fromUnit) {
            "km/s" -> quantity * 1000
            "km/h" -> quantity / 3.6
            "mi/h" -> quantity * 0.44704
            "nudos" -> quantity * 0.514444
            "mm/s" -> quantity / 1000
            "milla/s" -> quantity * 1609.34
            "pie/s" -> quantity * 0.3048
            else -> quantity // Si ya está en m/s
        }

        // Convertir de m/s a la unidad de destino
        return when (toUnit) {
            "km/s" -> result / 1000
            "km/h" -> result * 3.6
            "mi/h" -> result / 0.44704
            "nudos" -> result / 0.514444
            "mm/s" -> result * 1000
            "milla/s" -> result / 1609.34
            "pie/s" -> result / 0.3048
            else -> result // Si la unidad de destino es m/s
        }
    }
}
