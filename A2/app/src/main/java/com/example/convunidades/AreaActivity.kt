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

class AreaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_area)

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

        // Lista de unidades de área
        val units = arrayOf(
            "Metro cuadrado (m²)", "Kilómetro cuadrado (km²)", "Hectárea (ha)", "Centímetro cuadrado (cm²)",
            "Milímetro cuadrado (mm²)", "Pulgada cuadrada (in²)", "Pie cuadrado (ft²)", "Yarda cuadrada (yd²)",
            "Acre", "Milla cuadrada (mi²)", "Kilómetro cuadrado (km²)", "Are"
        )

        // Configurar los spinners
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, units)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFromUnit.adapter = adapter
        spinnerToUnit.adapter = adapter

        // Acción para el botón de retroceder
        btnBack.setOnClickListener {
            finish()// Regresa a la actividad anterior
        }

        // Acción para convertir
        btnConvert.setOnClickListener {
            val quantity = etQuantity.text.toString().toDoubleOrNull()
            if (quantity != null) {
                val fromUnit = spinnerFromUnit.selectedItem.toString()
                val toUnit = spinnerToUnit.selectedItem.toString()

                // Lógica de conversión
                val result = convertArea(quantity, fromUnit, toUnit)
                tvResult.text = "Resultado: $result $toUnit"
            } else {
                tvResult.text = "Por favor ingrese una cantidad válida"
            }
        }
    }

    // Función para convertir área
    private fun convertArea(quantity: Double, fromUnit: String, toUnit: String): Double {
        var result = quantity

        // Convertir a metro cuadrado (unidad base)
        result = when (fromUnit) {
            "Kilómetro cuadrado (km²)" -> quantity * 1_000_000
            "Hectárea (ha)" -> quantity * 10_000
            "Centímetro cuadrado (cm²)" -> quantity / 10_000
            "Milímetro cuadrado (mm²)" -> quantity / 1_000_000
            "Pulgada cuadrada (in²)" -> quantity * 0.00064516
            "Pie cuadrado (ft²)" -> quantity * 0.092903
            "Yarda cuadrada (yd²)" -> quantity * 0.836127
            "Acre" -> quantity * 4046.86
            "Milla cuadrada (mi²)" -> quantity * 2_589_988.11
            "Kilómetro cuadrado (km²)" -> quantity * 1_000_000
            "Are" -> quantity * 100
            else -> quantity // Si ya está en m²
        }

        // Convertir de m² a la unidad de destino
        return when (toUnit) {
            "Kilómetro cuadrado (km²)" -> result / 1_000_000
            "Hectárea (ha)" -> result / 10_000
            "Centímetro cuadrado (cm²)" -> result * 10_000
            "Milímetro cuadrado (mm²)" -> result * 1_000_000
            "Pulgada cuadrada (in²)" -> result / 0.00064516
            "Pie cuadrado (ft²)" -> result / 0.092903
            "Yarda cuadrada (yd²)" -> result / 0.836127
            "Acre" -> result / 4046.86
            "Milla cuadrada (mi²)" -> result / 2_589_988.11
            "Kilómetro cuadrado (km²)" -> result / 1_000_000
            "Are" -> result / 100
            else -> result // Si la unidad de destino es m²
        }
    }
}
