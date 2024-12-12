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

class TemperatureActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temperature)

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

        // Lista de unidades de temperatura
        val units = arrayOf("Celsius", "Fahrenheit", "Kelvin")

        // Configurar los spinners para unidades de origen y destino
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, units)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFromUnit.adapter = adapter
        spinnerToUnit.adapter = adapter

        // Acción para retroceder
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
                val result = convertTemperature(quantity, fromUnit, toUnit)
                tvResult.text = "Resultado: $result $toUnit"
            } else {
                tvResult.text = "Por favor ingrese una cantidad válida"
            }
        }
    }

    // Función para convertir temperatura
    private fun convertTemperature(quantity: Double, fromUnit: String, toUnit: String): Double {
        var result = quantity

        // Convertir a Celsius (unidad base)
        result = when (fromUnit) {
            "Fahrenheit" -> (quantity - 32) * 5 / 9
            "Kelvin" -> quantity - 273.15
            else -> quantity // Si ya está en Celsius
        }

        // Convertir de Celsius a la unidad de destino
        return when (toUnit) {
            "Fahrenheit" -> result * 9 / 5 + 32
            "Kelvin" -> result + 273.15
            else -> result // Si la unidad de destino es Celsius
        }
    }
}
