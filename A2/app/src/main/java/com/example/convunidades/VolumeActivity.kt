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

class VolumeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volume)

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

        // Lista de unidades de volumen
        val units = arrayOf(
            "Centímetro Cúbico (cm³)", "Mililitros (mL)", "Litros (L)", "Galón (gal)",
            "Pinta (pt)", "Taza (cup)", "Cucharada (tbsp)", "Cucharadita (tsp)",
            "Metro Cúbico (m³)", "Barriles (bbl)", "Onza líquida (fl oz)", "Decilitros (dL)"
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
                val result = convertVolume(quantity, fromUnit, toUnit)
                tvResult.text = "Resultado: $result $toUnit"
            } else {
                tvResult.text = "Por favor ingrese una cantidad válida"
            }
        }
    }

    // Función para convertir volumen
    private fun convertVolume(quantity: Double, fromUnit: String, toUnit: String): Double {
        var result = quantity

        // Convertir a litros (unidad base)
        result = when (fromUnit) {
            "Centímetro Cúbico (cm³)" -> quantity / 1000
            "Mililitros (mL)" -> quantity / 1000
            "Galón (gal)" -> quantity * 3.78541
            "Pinta (pt)" -> quantity * 0.473176
            "Taza (cup)" -> quantity * 0.236588
            "Cucharada (tbsp)" -> quantity * 0.0147868
            "Cucharadita (tsp)" -> quantity * 0.00492892
            "Metro Cúbico (m³)" -> quantity * 1000
            "Barriles (bbl)" -> quantity * 158.987
            "Onza líquida (fl oz)" -> quantity * 0.0295735
            "Decilitros (dL)" -> quantity * 0.1
            else -> quantity // Si ya está en Litros
        }

        // Convertir de litros a la unidad de destino
        return when (toUnit) {
            "Centímetro Cúbico (cm³)" -> result * 1000
            "Mililitros (mL)" -> result * 1000
            "Galón (gal)" -> result / 3.78541
            "Pinta (pt)" -> result / 0.473176
            "Taza (cup)" -> result / 0.236588
            "Cucharada (tbsp)" -> result / 0.0147868
            "Cucharadita (tsp)" -> result / 0.00492892
            "Metro Cúbico (m³)" -> result / 1000
            "Barriles (bbl)" -> result / 158.987
            "Onza líquida (fl oz)" -> result / 0.0295735
            "Decilitros (dL)" -> result / 0.1
            else -> result // Si la unidad de destino es Litros
        }
    }
}
