package com.example.preguntasyrespuestas.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.preguntasyrespuestas.R
import com.example.preguntasyrespuestas.ui.actividades.inside.JuegoActivity

class InicioFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Se obtiene la vista
        val vista = inflater.inflate(R.layout.fragment_inicio, container, false)

        // Se obtienen las preferencias y los atributos
        val prefs = activity?.getSharedPreferences("dataSession", 0)
        val nombre = prefs?.getString("nombreUsuario", null)
        val puntaje = prefs?.getInt("puntaje", -1)

        // Si el puntaje es negativo quiere decir que no hay ninguno guardado
        if (puntaje == -1)
        // Se llama a Volley para que obtenga el puntaje
            Volley.newRequestQueue(this.context).add(
                StringRequest("${getString(R.string.urlUsuario)}getPuntaje.php?nombre=$nombre",
                    // Se configuran los trofeos segun el puntaje
                    Response.Listener<String> { response ->
                        cambiarTrofeos(response.toInt(), vista)
                    },
                    Response.ErrorListener {
                        AlertDialog.Builder(this.context!!)
                            .setMessage("No se encuentra conexion, intente mas tarde.")
                            .setCancelable(false)
                            .setPositiveButton("Si") { _, _ ->
                                this.activity?.finish()
                            }.create().show()
                    })
            )
        else cambiarTrofeos(puntaje, vista)

        // Se escuchan los botones de las imagenes para mostrar los puntajes necesarios
        vista.findViewById<ImageView>(R.id.t1)
            .setOnClickListener { mostrarToast("Necesario puntaje mayor 100") }
        vista.findViewById<ImageView>(R.id.t2)
            .setOnClickListener { mostrarToast("Necesario puntaje mayor 200") }
        vista.findViewById<ImageView>(R.id.t3)
            .setOnClickListener { mostrarToast("Necesario puntaje mayor 300") }
        vista.findViewById<ImageView>(R.id.t4)
            .setOnClickListener { mostrarToast("Necesario puntaje mayor 400") }
        vista.findViewById<ImageView>(R.id.t5)
            .setOnClickListener { mostrarToast("Necesario puntaje mayor 500") }

        vista.findViewById<Button>(R.id.btnIniciarJuego).setOnClickListener {
            // se inicia el juego
            startActivity(Intent(activity, JuegoActivity::class.java))
            this.activity?.finish()
        }

        return vista
    }

    private fun cambiarTrofeos(puntaje: Int?, vista: View) {
        // Segun el puntaje que active o desactive los trofeos
        when {
            puntaje!! >= 500 -> {
                vista.findViewById<ImageView>(R.id.t5).setImageResource(R.drawable.trophy5)
                vista.findViewById<ImageView>(R.id.t4).setImageResource(R.drawable.trophy4)
                vista.findViewById<ImageView>(R.id.t3).setImageResource(R.drawable.trophy3)
                vista.findViewById<ImageView>(R.id.t2).setImageResource(R.drawable.trophy2)
                vista.findViewById<ImageView>(R.id.t1).setImageResource(R.drawable.trophy1)
            }
            puntaje >= 400 -> {
                vista.findViewById<ImageView>(R.id.t4).setImageResource(R.drawable.trophy4)
                vista.findViewById<ImageView>(R.id.t3).setImageResource(R.drawable.trophy3)
                vista.findViewById<ImageView>(R.id.t2).setImageResource(R.drawable.trophy2)
                vista.findViewById<ImageView>(R.id.t1).setImageResource(R.drawable.trophy1)
            }
            puntaje >= 300 -> {
                vista.findViewById<ImageView>(R.id.t3).setImageResource(R.drawable.trophy3)
                vista.findViewById<ImageView>(R.id.t2).setImageResource(R.drawable.trophy2)
                vista.findViewById<ImageView>(R.id.t1).setImageResource(R.drawable.trophy1)
            }
            puntaje >= 200 -> {
                vista.findViewById<ImageView>(R.id.t2).setImageResource(R.drawable.trophy2)
                vista.findViewById<ImageView>(R.id.t1).setImageResource(R.drawable.trophy1)
            }
            puntaje >= 100 -> vista.findViewById<ImageView>(R.id.t1).setImageResource(R.drawable.trophy1)
        }

    }

    private fun mostrarToast(mensaje: String) {
        Toast.makeText(this.context, mensaje, Toast.LENGTH_LONG).show()
    }

}