package com.example.tictactoe
import androidx.core.content.ContextCompat

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class OnePlayerActivity : AppCompatActivity() {

    private lateinit var gridLayout: Array<Array<Button>>
    private lateinit var statusText: TextView
    private var currentPlayer = 1 // 1 for Player (X), 2 for AI (O)
    private val board = Array(3) { Array(3) { 0 } } // 0 for empty, 1 for Player (X), 2 for AI (O)
    private var gameOver = false  // Variable para comprobar si el juego ha terminado

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one_player)

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

    // Método para manejar el clic en una celda
    private fun cellClicked(row: Int, col: Int) {
        // Si el juego ya ha terminado o la celda está ocupada, no hacer nada
        if (gameOver || board[row][col] != 0) return

        // Actualizar el tablero
        board[row][col] = currentPlayer
        gridLayout[row][col].text = if (currentPlayer == 1) "X" else "O"
        gridLayout[row][col].setBackgroundColor(resources.getColor(R.color.dark_gray))  // Cambiar color a plomo oscuro

        // Comprobar si el jugador ha ganado
        if (checkWin(currentPlayer)) {
            showWinnerDialog(currentPlayer)
            gameOver = true
            return
        }

        // Comprobar si es empate
        if (isBoardFull()) {
            showTieDialog()
            gameOver = true
            return
        }


        currentPlayer = 2
        statusText.text = "Turno Máquina!"
        disableBoard()


        Handler().postDelayed({
            aiTurn()
        }, 1000)  // 1 segundo de espera antes de que la IA haga su jugada
    }


    private fun aiTurn() {

        val bestMove = findBestMove()
        val row = bestMove.first
        val col = bestMove.second


        board[row][col] = 2
        gridLayout[row][col].text = "O"
        gridLayout[row][col].setBackgroundColor(resources.getColor(R.color.dark_gray))  // Cambiar color a plomo oscuro


        if (checkWin(2)) {
            showWinnerDialog(2)
            gameOver = true
            return
        }

        // Comprobar si es empate
        if (isBoardFull()) {
            showTieDialog()
            gameOver = true
            return
        }

        // Cambiar al turno del jugador
        currentPlayer = 1
        statusText.text = "Jugador, tu turno!"
        enableBoard()  // Habilitar el tablero para que el jugador pueda jugar
    }


    private fun findBestMove(): Pair<Int, Int> {
        var bestVal = Int.MIN_VALUE
        var bestMove = Pair(-1, -1)

        // Recorre todas las celdas disponibles y evalúa cada una
        for (i in 0..2) {
            for (j in 0..2) {
                if (board[i][j] == 0) {
                    // Simula el movimiento
                    board[i][j] = 2
                    val moveVal = minimax(board, 0, false)
                    board[i][j] = 0 // Deshace el movimiento

                    // Elige el mejor movimiento
                    if (moveVal > bestVal) {
                        bestMove = Pair(i, j)
                        bestVal = moveVal
                    }
                }
            }
        }

        return bestMove
    }


    private fun minimax(board: Array<Array<Int>>, depth: Int, isMax: Boolean): Int {

        val score = evaluate(board)
        if (score == 10) return score
        if (score == -10) return score

        if (isBoardFull()) return 0


        if (isMax) {
            var best = Int.MIN_VALUE
            for (i in 0..2) {
                for (j in 0..2) {
                    if (board[i][j] == 0) {
                        board[i][j] = 2
                        best = maxOf(best, minimax(board, depth + 1, false))
                        board[i][j] = 0
                    }
                }
            }
            return best
        }

        else {
            var best = Int.MAX_VALUE
            for (i in 0..2) {
                for (j in 0..2) {
                    if (board[i][j] == 0) {
                        board[i][j] = 1
                        best = minOf(best, minimax(board, depth + 1, true))
                        board[i][j] = 0
                    }
                }
            }
            return best
        }
    }

    // Evaluar el estado del tablero
    private fun evaluate(board: Array<Array<Int>>): Int {
        // Comprobar filas
        for (row in 0..2) {
            if (board[row][0] == board[row][1] && board[row][1] == board[row][2]) {
                if (board[row][0] == 2) return 10
                if (board[row][0] == 1) return -10
            }
        }
        // Comprobar columnas
        for (col in 0..2) {
            if (board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
                if (board[0][col] == 2) return 10
                if (board[0][col] == 1) return -10
            }
        }
        // Comprobar diagonales
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            if (board[0][0] == 2) return 10
            if (board[0][0] == 1) return -10
        }
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            if (board[0][2] == 2) return 10
            if (board[0][2] == 1) return -10
        }

        return 0
    }


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

    // Habilitar el tablero para el jugador
    private fun enableBoard() {
        for (i in 0..2) {
            for (j in 0..2) {
                if (board[i][j] == 0) {
                    gridLayout[i][j].isEnabled = true
                }
            }
        }
    }

    // Mostrar un AlertDialog cuando un jugador gane
    private fun showWinnerDialog(player: Int) {
        val winner = if (player == 1) "Jugador (X)" else "Máquina (O)"

        val builder = AlertDialog.Builder(this)
        builder.setTitle("¡Ganador!")
        builder.setMessage("$winner ha ganado el juego.")

        // Botón para volver a jugar
        builder.setPositiveButton("Volver a jugar") { dialog, _ ->
            dialog.dismiss()
            resetGame() // Reiniciar el juego
        }

        // Botón para ir al menú
        builder.setNegativeButton("Ir al menú") { dialog, _ ->
            dialog.dismiss()
            goToMenu() // Volver al menú
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
            resetGame() // Reiniciar el juego
        }

        // Botón para ir al menú
        builder.setNegativeButton("Ir al menú") { dialog, _ ->
            dialog.dismiss()
            goToMenu() // Volver al menú
        }

        val dialog = builder.create()
        dialog.show()
    }

    // Reiniciar el juego
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
        statusText.text = "Jugador, tu turno!"
        gameOver = false  // Reiniciar el estado del juego
    }

    // Navegar al menú principal
    private fun goToMenu() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Finaliza la actividad actual
    }
}
