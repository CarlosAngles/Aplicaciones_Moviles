package com.example.tictactoe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Configuración de padding para Edge to Edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Botón: Un jugador
        val btnOnePlayer = findViewById<Button>(R.id.btn_one_player)
        btnOnePlayer.setOnClickListener {
            // Navegar a la actividad para el juego de un jugador
            val intent = Intent(this, OnePlayerActivity::class.java)
            startActivity(intent)
        }

        // Botón: Dos jugadores
        val btnTwoPlayers = findViewById<Button>(R.id.btn_two_players)
        btnTwoPlayers.setOnClickListener {
            // Navegar a la actividad para el juego de dos jugadores
            val intent = Intent(this, TwoPlayersActivity::class.java)
            startActivity(intent)
        }

        // Botón: Salir
        val btnExit = findViewById<Button>(R.id.btn_exit)
        btnExit.setOnClickListener {
            finishAffinity() // Cierra la actividad y todas las actividades relacionadas
            System.exit(0)   // Finaliza el proceso de la aplicación
        }
    }
}
