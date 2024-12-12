package com.example.micalculadora

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import net.objecthunter.exp4j.ExpressionBuilder
import android.widget.Button

class GraphActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)

        val expressionStr = intent.getStringExtra("EXPRESSION") ?: ""

        val chart: LineChart = findViewById(R.id.chart)
        plotExpression(chart, expressionStr)


        val btnBackTop: Button = findViewById(R.id.btnBackTop)
        btnBackTop.setOnClickListener {
            finish()
        }
    }

    private fun plotExpression(chart: LineChart, expressionStr: String) {
        val entries = ArrayList<Entry>()

        val startX = -10.0
        val endX = 10.0
        val step = 0.1

        var x = startX
        while (x <= endX) {
            try {
                val expression = ExpressionBuilder(expressionStr).variables("x").build()
                val y = expression.setVariable("x", x).evaluate()

                // Ignorar valores inválidos
                if (y.isInfinite() || y.isNaN()) {
                    x += step
                    continue
                }

                entries.add(Entry(x.toFloat(), y.toFloat()))
            } catch (e: Exception) {
                Toast.makeText(this, "Error en la expresión: ${e.message}", Toast.LENGTH_SHORT).show()
                return
            }
            x += step
        }

        val dataSet = LineDataSet(entries, "y = $expressionStr").apply {
            color = resources.getColor(R.color.purple_200, theme)
            lineWidth = 2f
            setDrawCircles(false)
        }

        val lineData = LineData(dataSet)
        chart.data = lineData
        dataSet.setDrawValues(false)
        // Configuraciones del gráfico
        chart.xAxis.axisMinimum = startX.toFloat()
        chart.xAxis.axisMaximum = endX.toFloat()
        chart.axisLeft.axisMinimum = -50f  // Ajusta según el rango esperado
        chart.axisLeft.axisMaximum = 50f   // Ajusta según el rango esperado
        chart.axisRight.isEnabled = false
        chart.setTouchEnabled(true)
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)
        chart.setPinchZoom(true)
        chart.description.isEnabled = false

        chart.invalidate()  // Refresca el gráfico
    }

}