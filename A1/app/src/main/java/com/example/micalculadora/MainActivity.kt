package com.example.micalculadora

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import net.objecthunter.exp4j.ExpressionBuilder // Usaremos esta librería para evaluar la expresión
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.lang.Exception
import android.content.Intent

class MainActivity : AppCompatActivity() {
    private lateinit var tvResult: TextView
    private lateinit var tvOperation: TextView
    private var isNewOp: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvResult = findViewById(R.id.tvResult)
        tvOperation = findViewById(R.id.tvOperation)
        clear() // Inicializa la pantalla

        // Configuración de botones de la calculadora
        val buttons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
            R.id.btnAdd, R.id.btnSubtract, R.id.btnMultiply,
            R.id.btnDivide, R.id.btnPower, R.id.btnEquals,
            R.id.btnClear, R.id.btnDecimal, R.id.btnRaiz,
            R.id.btnOpenParen, R.id.btnCloseParen, R.id.btnGraficar, R.id.btnx
        )

        // Inicializar botones de número y operaciones
        buttons.forEach { buttonId ->
            findViewById<Button>(buttonId).setOnClickListener {
                when (buttonId) {
                    R.id.btn0 -> appendToOperation("0")
                    R.id.btn1 -> appendToOperation("1")
                    R.id.btn2 -> appendToOperation("2")
                    R.id.btn3 -> appendToOperation("3")
                    R.id.btn4 -> appendToOperation("4")
                    R.id.btn5 -> appendToOperation("5")
                    R.id.btn6 -> appendToOperation("6")
                    R.id.btn7 -> appendToOperation("7")
                    R.id.btn8 -> appendToOperation("8")
                    R.id.btn9 -> appendToOperation("9")
                    R.id.btnAdd -> appendToOperation("+")
                    R.id.btnSubtract -> appendToOperation("-")
                    R.id.btnMultiply -> appendToOperation("*")
                    R.id.btnDivide -> appendToOperation("/")
                    R.id.btnPower -> appendToOperation("^")
                    R.id.btnOpenParen -> appendToOperation("(")
                    R.id.btnCloseParen -> appendToOperation(")")
                    R.id.btnDecimal -> appendToOperation(".")
                    R.id.btnEquals -> calculateResult()
                    R.id.btnClear -> clear()
                    R.id.btnRaiz -> calculateSqrt()
                    R.id.btnGraficar -> plotExpression()
                    R.id.btnx -> appendToOperation("x")
                }
            }
        }

    }

    private fun setupChart(chart: LineChart) {
        // Configuración básica del gráfico
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.axisRight.isEnabled = false
        chart.description.isEnabled = false
    }

    private fun appendToOperation(value: String) {
        if (isNewOp) {
            tvOperation.text = value
            isNewOp = false
        } else {
            tvOperation.text = "${tvOperation.text}$value"
        }
    }

    private fun calculateSqrt() {
        appendToOperation("sqrt(")
    }

    private fun calculateResult() {
        try {
            val expression = ExpressionBuilder(tvOperation.text.toString()).build()
            val result = expression.evaluate()
            tvResult.text = result.toString()
            isNewOp = true
        } catch (e: Exception) {
            tvResult.text = "Error"
            isNewOp = true
        }
    }

    private fun clear() {
        tvResult.text = "0"
        tvOperation.text = ""
        isNewOp = true
    }

    private fun plotExpression() {
        val expressionStr = tvOperation.text.toString()
        if (expressionStr.isNotEmpty()) {
            // Crea un Intent para lanzar la nueva actividad
            val intent = Intent(this, GraphActivity::class.java)
            // Pasa la expresión a la nueva actividad
            intent.putExtra("EXPRESSION", expressionStr)
            // Lanza la nueva actividad
            startActivity(intent)
        } else {
            Toast.makeText(this, "Por favor ingresa una expresión válida", Toast.LENGTH_SHORT).show()
        }
    }


}