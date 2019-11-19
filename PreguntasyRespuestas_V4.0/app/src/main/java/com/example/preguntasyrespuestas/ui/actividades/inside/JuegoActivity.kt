package com.example.preguntasyrespuestas.ui.actividades.inside

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.preguntasyrespuestas.clases.Pregunta
import com.example.preguntasyrespuestas.R
import kotlinx.android.synthetic.main.activity_juego.*

class JuegoActivity : AppCompatActivity() {

    // Declaracion de atributos globales
    // Lista instanseada de la clase preguntas
    private val listaPreguntas = mutableListOf<Pregunta>()
    // Contador segun preguntas
    private var cont = 0
    // acumulador de puntaje
    private var puntaje = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_juego)

        // Se realzia una llamada a volley retornando las preguntas
        Volley.newRequestQueue(this).add(
            StringRequest("${getString(R.string.urlPregunta)}getPreguntas.php",
                // Se construye las preguntas con el texto retornado
                Response.Listener<String> { response -> construirPreguntas(response) },
                // Se muestra mensaje de error
                Response.ErrorListener { mostrarDialog() })
        )
    }

    // Funcion que construyen las preguntas con la lectura de Volley
    private fun construirPreguntas(lineas: String) {
        // Separa el texto obtenido segun los %% que hayan para separar las preguntas
        val cantPreguntas = lineas.split("%%")
        var datos: List<String>

        // Se recorren las preguntas
        for (i in (0..cantPreguntas.size - 2)) {
            // Se guardan los atributos de la preguntas separados por <>
            datos = cantPreguntas[i].split("<>")
            // Se crea la pregunta
            listaPreguntas.add(
                Pregunta(
                    datos[0],
                    datos[1],
                    datos[2]
                )
            )
        }

        // Se mezclan las preguntas
        listaPreguntas.shuffle()

        // Se llama a la funcion jugar
        jugar()
    }

    // Funcion donde se configura la interfaz
    private fun jugar() {
        // La jugabilidad es de 5 preguntas continuas
        if (cont < 5) {
            // Se da el titulo
            tituloPregunta.setText(listaPreguntas[cont].getTitulo())

            // Se obtienen las opciones de respuesta separadas por |
            val preguntasInternas = listaPreguntas[cont].getRespuesta().split("|")

            // Se mezcla el orden de las opciones
            preguntasInternas.shuffled()

            // Se dan las opciones
            res1.text = preguntasInternas[0]
            res2.text = preguntasInternas[1]
            res3.text = preguntasInternas[2]
            res4.text = preguntasInternas[3]

            // Se configuran otros aspectos como el numero de la pregunta y el puntaje
            numPregunta.text = "Pregunta: " + (cont + 1) + "."
            puntajeV.text = "Puntaje actual: $puntaje."

            // Se escuchan todos los botones
            res1.setOnClickListener { validarRespuesta(res1.text.toString()) }
            res2.setOnClickListener { validarRespuesta(res2.text.toString()) }
            res3.setOnClickListener { validarRespuesta(res3.text.toString()) }
            res4.setOnClickListener { validarRespuesta(res4.text.toString()) }

            // Se escucha el boton omitir
            btnOmitir.setOnClickListener {
                // Se avanza la pregunta
                cont++
                // Se resta 4 al puntaje como penalizacion
                puntaje -= 4
                // Se llama a si misma para continuar con la siguiente pregunta
                jugar()
            }
        } else final()
    }

    // La funcion final encargada de llenar las preferencias y actualoizar los datos
    private fun final() {
        // Se actualiza el puntaje por ultima vez
        puntajeV.text = "Puntaje actual: $puntaje."
        // Se obtienen las preferencias
        val prefs = getSharedPreferences("dataSession", 0)
        // Se obtiene el nombre segun las preferencias
        val nombre = prefs.getString("nombreUsuario", null)
        // Se calcula el nuevo puntaje total
        val nuevoPuntaje = prefs.getInt("puntaje", 0) + puntaje

        // Se llama volley para actualizar el puntaje en la base de datos
        Volley.newRequestQueue(this).add(
            StringRequest("${getString(R.string.urlUsuario)}cambio.php?nombre=$nombre&dato=$nuevoPuntaje&atributo=2",
                Response.Listener<String> {},
                Response.ErrorListener { mostrarDialog() })
        )
        // Se actualiza el puntaje en las referencias
        prefs.edit().putInt("puntaje", nuevoPuntaje).apply()

        // Se crea una dialogo en donde se enseña el puntaje total final
        AlertDialog.Builder(this).setMessage("Su puntaje obtenido es de: $puntaje.")
            .setCancelable(false)
            // Se escucha el boton positivo
            .setPositiveButton("Ok") { _, _ ->
                finish()
                startActivity(Intent(this, InsideActivity::class.java))
            }.create().show()
    }

    // Funcion donde se valida la respuesta escogida por el jugador
    private fun validarRespuesta(respuesta: String) {
        // Si la respuestas es igual a el atributo verdadero de preguntas
        if (listaPreguntas[cont].getVerdadera().toLowerCase() == respuesta.toLowerCase()) {
            // Se muestra mensaje
            mostrarToast("Verdadero")
            // Puntaje aumenta en 5
            puntaje += 5
            // Contador aumenta en 1 para la siguiente pregunta
            cont++
            // Se llama a jugar
            jugar()
        } else {
            mostrarToast("Falso")
            // Se penaliza por equivocacion cometida y se disminuyen 3 puntos
            puntaje -= 3
            // Se actualiza el puntaje actual en la interfaz
            puntajeV.text = "Puntaje actual: $puntaje."
        }
    }

    private fun mostrarDialog() {
        AlertDialog.Builder(this)
            .setMessage("No se encuentra conexion, intente mas tarde.")
            .setCancelable(false)
            .setPositiveButton("Si") { _, _ ->
                finish()
            }.create().show()
    }

    // Funcion que muestra mensajes
    private fun mostrarToast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

    // Capturar el evento "atras" en esta activity
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        // Si se uso el boton/gesto atras
        if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
            // Se declara una variable que cambiara segun la opcin escogida
            var bandera = true

            // se crea una alerta
            AlertDialog.Builder(this)
                .setMessage("¿Desea sali? esto tendría una penalización de -5 puntos.")
                .setCancelable(false)
                .setPositiveButton("Si") { _, _ ->
                    // Si escogio Si y el puntaje es mayor a 0 entonces ahora equivaldria a -5
                    if (puntaje >= 0)
                        puntaje = -5
                    else
                    // si el puntaje es negativo se le restara -5 mas
                        puntaje -= 5
                    final()
                }.setNegativeButton("No") { _, _ ->
                    // Si escogio No entonces se declara la bandera como falsa
                    bandera = false
                }.create().show()

            // Si la bandera es falsa que no se realiza el evento de retroceder
            if (!bandera)
                return true
        }
        // Ralizacion normal del evento
        return super.onKeyDown(keyCode, event)
    }
}
