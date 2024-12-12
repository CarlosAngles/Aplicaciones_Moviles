package com.example.convunidades

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LengthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_length)

        // Obtener los elementos de la interfaz
        val btnBack = findViewById<Button>(R.id.btn_back)
        val etQuantity = findViewById<EditText>(R.id.et_quantity)
        val spinnerFromUnit = findViewById<Spinner>(R.id.spinner_from_unit)
        val spinnerToUnit = findViewById<Spinner>(R.id.spinner_to_unit)
        val btnConvert = findViewById<Button>(R.id.btn_convert)
        val tvResult = findViewById<TextView>(R.id.tv_result)

        // Lista de unidades de longitud, ahora con Decímetro y abreviaciones
        val units = arrayOf(
            "Metros (m)", "Kilómetros (km)", "Centímetros (cm)", "Milímetros (mm)",
            "Decímetros (dm)", "Millas (mi)", "Yardas (yd)", "Pulgadas (in)", "Pies (ft)"
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
                val result = convertLength(quantity, fromUnit, toUnit)
                tvResult.text = "Resultado: $result $toUnit"
            } else {
                tvResult.text = "Por favor ingrese una cantidad válida"
            }
        }
    }


    private fun convertLength(quantity: Double, fromUnit: String, toUnit: String): Double {
        var result = quantity


        result = when (fromUnit) {
            "Kilómetros (km)" -> quantity * 1000
            "Centímetros (cm)" -> quantity / 100
            "Milímetros (mm)" -> quantity / 1000
            "Decímetros (dm)" -> quantity / 10
            "Millas (mi)" -> quantity * 1609.34
            "Yardas (yd)" -> quantity * 0.9144
            "Pulgadas (in)" -> quantity * 0.0254
            "Pies (ft)" -> quantity * 0.3048
            else -> quantity
        }


        return when (toUnit) {
            "Kilómetros (km)" -> result / 1000
            "Centímetros (cm)" -> result * 100
            "Milímetros (mm)" -> result * 1000
            "Decímetros (dm)" -> result * 10
            "Millas (mi)" -> result / 1609.34
            "Yardas (yd)" -> result / 0.9144
            "Pulgadas (in)" -> result / 0.0254
            "Pies (ft)" -> result / 0.3048
            else -> result
        }
    }
}
