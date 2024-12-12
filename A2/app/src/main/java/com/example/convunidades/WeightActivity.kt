package com.example.convunidades

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class WeightActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_weight)

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

        // Lista de unidades de peso/masa con sus abreviaturas
        val units = arrayOf("Kilogramos (kg)", "Gramos (g)", "Miligramo (mg)", "Libras (lb)", "Onzas (oz)", "Toneladas (t)")

        // Configurar los spinners
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, units)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFromUnit.adapter = adapter
        spinnerToUnit.adapter = adapter

        // Acción del botón de retroceso
        btnBack.setOnClickListener {
            finish()// Regresa a la actividad anterior
        }

        // Acción del botón de conversión
        btnConvert.setOnClickListener {
            val quantity = etQuantity.text.toString().toDoubleOrNull()
            if (quantity != null) {
                val fromUnit = spinnerFromUnit.selectedItem.toString().split(" ")[0] // Obtiene solo la unidad, sin la abreviatura
                val toUnit = spinnerToUnit.selectedItem.toString().split(" ")[0] // Obtiene solo la unidad, sin la abreviatura

                // Lógica de conversión
                val result = convertWeight(quantity, fromUnit, toUnit)
                tvResult.text = "Resultado: $result $toUnit"
            } else {
                tvResult.text = "Por favor ingrese una cantidad válida"
            }
        }
    }

    // Función para convertir peso/masa
    private fun convertWeight(quantity: Double, fromUnit: String, toUnit: String): Double {
        var result = quantity

        // Convertir a kilogramos (unidad base)
        result = when (fromUnit) {
            "Gramos" -> quantity / 1000
            "Miligramo" -> quantity / 1_000_000
            "Libras" -> quantity * 0.453592
            "Onzas" -> quantity * 0.0283495
            "Toneladas" -> quantity * 1000
            else -> quantity // Si ya está en kilogramos
        }

        // Convertir de kilogramos a la unidad de destino
        return when (toUnit) {
            "Gramos" -> result * 1000
            "Miligramo" -> result * 1_000_000
            "Libras" -> result / 0.453592
            "Onzas" -> result / 0.0283495
            "Toneladas" -> result / 1000
            else -> result // Si la unidad de destino es kilogramos
        }
    }
}
