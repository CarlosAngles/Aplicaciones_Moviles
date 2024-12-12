package com.example.convunidades

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Configuración de la vista para aplicar los márgenes de las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtener los botones por ID
        val btnLength = findViewById<Button>(R.id.btn_length)
        val btnWeight = findViewById<Button>(R.id.btn_weight)
        val btnTemperature = findViewById<Button>(R.id.btn_temperature)
        val btnVolume = findViewById<Button>(R.id.btn_volume)
        val btnSpeed = findViewById<Button>(R.id.btn_speed)
        val btnArea = findViewById<Button>(R.id.btn_area)
        val btnTime = findViewById<Button>(R.id.btn_time)

        // Configurar los listeners de clic para cada botón
        btnLength.setOnClickListener {
            val intent = Intent(this, LengthActivity::class.java)
            startActivity(intent)
        }

        btnWeight.setOnClickListener {
            val intent = Intent(this, WeightActivity::class.java)
            startActivity(intent)
        }

        btnTemperature.setOnClickListener {
            val intent = Intent(this, TemperatureActivity::class.java)
            startActivity(intent)
        }

        btnVolume.setOnClickListener {
            val intent = Intent(this, VolumeActivity::class.java)
            startActivity(intent)
        }

        btnSpeed.setOnClickListener {
            val intent = Intent(this, SpeedActivity::class.java)
            startActivity(intent)
        }

        btnArea.setOnClickListener {
            val intent = Intent(this, AreaActivity::class.java)
            startActivity(intent)
        }

        btnTime.setOnClickListener {
            val intent = Intent(this, TimeActivity::class.java)
            startActivity(intent)
        }
    }
}
