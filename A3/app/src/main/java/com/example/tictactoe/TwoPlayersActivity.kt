package com.example.tictactoe
import androidx.core.content.ContextCompat
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog

class TwoPlayersActivity : AppCompatActivity() {

    private lateinit var gridLayout: Array<Array<Button>>
    private lateinit var statusText: TextView
    private var currentPlayer = 1 // 1 for Player 1 (X), 2 for Player 2 (O)
    private val board = Array(3) { Array(3) { 0 } } // 0 for empty, 1 for Player 1 (X), 2 for Player 2 (O)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_two_players)

        // Inicialización de las vistas
        gridLayout = arrayOf(
            arrayOf(
                findViewById(R.id.cell_00), findViewById(R.id.cell_01), findViewById(R.id.cell_02)
            ),
            arrayOf(
                findViewById(R.id.cell_10), findViewById(R.id.cell_11), findViewById(R.id.cell_12)
            ),
            arrayOf(
                findViewById(R.id.cell_20), findViewById(R.id.cell_21), findViewById(R.id.cell_22)
            )
        )
        statusText = findViewById(R.id.statusText)

        // Configurar las celdas para manejar clics
        for (i in 0..2) {
            for (j in 0..2) {
                gridLayout[i][j].setOnClickListener { cellClicked(i, j) }
            }
        }
    }
    fun goToMenu(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Finaliza la actividad actual
    }

    // Método para manejar el clic en una celda
    private fun cellClicked(row: Int, col: Int) {
        // Si la celda ya está ocupada, no hacer nada
        if (board[row][col] != 0) return

        // Actualizar el tablero
        board[row][col] = currentPlayer
        gridLayout[row][col].text = if (currentPlayer == 1) "X" else "O"
        gridLayout[row][col].setBackgroundColor(resources.getColor(R.color.dark_gray))  // Cambiar color a plomo oscuro
        // Comprobar si el jugador actual ha ganado
        if (checkWin(currentPlayer)) {
            // Mostrar el diálogo con el jugador ganador
            showWinnerDialog(currentPlayer)
            disableBoard()
            return
        }

        // Comprobar si es empate
        if (isBoardFull()) {
            showTieDialog() // Mostrar mensaje de empate
            return
        }

        // Cambiar de jugador
        currentPlayer = if (currentPlayer == 1) 2 else 1
        statusText.text = "Jugador $currentPlayer (${if (currentPlayer == 1) "X" else "O"}), tu turno!"

    }

    // Comprobar si el jugador actual ha ganado
    private fun checkWin(player: Int): Boolean {
        // Comprobar filas
        for (i in 0..2) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) return true
        }
        // Comprobar columnas
        for (i in 0..2) {
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) return true
        }
        // Comprobar diagonales
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) return true
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) return true

        return false
    }

    // Comprobar si el tablero está lleno
    private fun isBoardFull(): Boolean {
        for (i in 0..2) {
            for (j in 0..2) {
                if (board[i][j] == 0) return false
            }
        }
        return true
    }

    // Desactivar el tablero después de que termine el juego
    private fun disableBoard() {
        for (i in 0..2) {
            for (j in 0..2) {
                gridLayout[i][j].isEnabled = false
            }
        }
    }


    // Mostrar un AlertDialog cuando un jugador gane
    private fun showWinnerDialog(player: Int) {
        val winner = if (player == 1) "Jugador 1 (X)" else "Jugador 2 (O)"

        // Crear un diálogo de alerta
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¡Ganador!")
        builder.setMessage("$winner ha ganado el juego.")

        // Botón para volver a jugar
        builder.setPositiveButton("Volver a jugar") { dialog, _ ->
            dialog.dismiss()
            resetGame() // Reiniciar el juego después de mostrar el mensaje
        }

        // Botón para ir al menú
        builder.setNegativeButton("Ir al menú") { dialog, _ ->
            dialog.dismiss()
            goToMenu() // Ir al menú
        }

        val dialog = builder.create()
        dialog.show()
    }

    // Mostrar un AlertDialog cuando sea empate
    private fun showTieDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¡Empate!")
        builder.setMessage("El juego ha terminado en empate.")

        // Botón para volver a jugar
        builder.setPositiveButton("Volver a jugar") { dialog, _ ->
            dialog.dismiss()
            resetGame() // Reiniciar el juego después de mostrar el mensaje
        }

        // Botón para ir al menú
        builder.setNegativeButton("Ir al menú") { dialog, _ ->
            dialog.dismiss()
            goToMenu() // Ir al menú
        }

        val dialog = builder.create()
        dialog.show()
    }

    // Reiniciar el juego después de que un jugador gane
    private fun resetGame() {
        for (i in 0..2) {
            for (j in 0..2) {
                board[i][j] = 0
                gridLayout[i][j].text = ""
                gridLayout[i][j].isEnabled = true
                gridLayout[i][j].background = ContextCompat.getDrawable(this, R.drawable.board_cell_border)

            }
        }
        currentPlayer = 1
        statusText.text = "Jugador 1, tu turno!"
    }

    // Navegar al menú principal
    private fun goToMenu() {
        // Aquí se asume que el menú es la MainActivity o cualquier otra actividad que hayas creado
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Finaliza la actividad actual
    }
}
