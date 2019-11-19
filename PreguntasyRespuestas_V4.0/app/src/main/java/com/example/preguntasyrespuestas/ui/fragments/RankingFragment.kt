package com.example.preguntasyrespuestas.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.preguntasyrespuestas.R
import com.example.preguntasyrespuestas.clases.Usuario
import kotlinx.android.synthetic.main.fragment_ranking.*

class RankingFragment : Fragment() {

    // Se declaran atributos
    private val listaUsuarios = mutableListOf<Usuario>()
    private var listaPuntajes = mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Se definen variables locales como las preferencias, la vista y el nombre del usuario actual
        val vista = inflater.inflate(R.layout.fragment_ranking, container, false)
        val prefs = activity?.getSharedPreferences("dataSession", 0)
        val nombre = prefs?.getString("nombreUsuario", null)

        // Se llama a Volley para obtener una lista de usuarios
        Volley.newRequestQueue(this.context).add(
            StringRequest("${getString(R.string.urlUsuario)}getPointsUsers.php?nombre=$nombre",
                Response.Listener<String> { response ->
                    // Se separan los usuarios del texto con ,
                    val usuario = response.split(",")
                    // Se declara una lista de atributos
                    var atributosUsuario: List<String>

                    // Se recorren los usuarios menos la ultima posicion que esta vacia y menos la primera posicion ya que iniciamos en 0
                    for (i in (0..usuario.size - 2)) {
                        // Se almacenan los atributos en el arreglo separando el usuario en -
                        atributosUsuario = usuario[i].split("-")
                        // Se agrega un puntaje a la lista de puntajes
                        listaPuntajes.add(atributosUsuario[1].toInt())
                        // Se agrega un usuario a la lista de usuarios
                        listaUsuarios.add(
                            // Se llama al constructor y se envia por parametro los atributos
                            Usuario(
                                atributosUsuario[0],
                                atributosUsuario[1].toInt(),
                                atributosUsuario[2].toInt()
                            )
                        )
                        // Cuando tengamos 5, se detiene el ciclo, solo promiaremos 5
                    }

                    // Se configura la interfaz
                    configurarInterfaz(vista, nombre)
                },
                // Aviso de error de conexion
                Response.ErrorListener {
                    AlertDialog.Builder(this.context!!)
                        .setMessage("No se encuentra conexion, intente mas tarde.")
                        .setCancelable(false)
                        .setPositiveButton("Si") { _, _ ->
                            this.activity?.finish()
                        }.create().show()
                })
        )
        return vista
    }

    private fun configurarInterfaz(vista: View, nombre: String?) {
        // Se escuchan las imagenes como botones para mostrar informacion
        vista.findViewById<ImageView>(R.id.imageView4)
            .setOnClickListener { mostrarToast("Medalla de oro") }
        vista.findViewById<ImageView>(R.id.imageView6)
            .setOnClickListener { mostrarToast("Medalla de plata") }
        vista.findViewById<ImageView>(R.id.imageView5)
            .setOnClickListener { mostrarToast("Medalla de bronce") }

        // Se ordena la lista de puntajes de mayor a menor
        listaPuntajes.sortDescending()
        // Se crea una variable contador y se inicializa en 0
        var cont = 0
        // Se crea una bandera que servira para cambiar el tamaño de la letra en caso de que el usuario este en el ranking
        var bandera = false

        // Se recorren los puntjaes
        for (i in listaPuntajes)
        // Se recorren los usuarios
            for (j in listaUsuarios)
            // Si el puntaje es igual al puntaje de la clase persona
                if (i == j.getPuntaje()) {
                    // Si el nombre del usuario con este puntaje es igual al usuario actual
                    if (j.getNombre() == nombre)
                    // Bandera true
                        bandera = true
                    // Segun el contador se valida en que posicion esta
                    when (cont) {
                        // Posicion 1
                        0 -> {
                            imgPos1.setImageResource(j.getImg())
                            txtNombrePos1.text = j.getNombre()
                            txtPuntajePos1.text = j.getPuntaje().toString()
                            // Si la bandera es true entonces que cambie el tamaño de la letra
                            if (bandera) txtNombrePos1.textSize = 18.0F
                        }
                        // Posicion 2
                        1 -> {
                            imgPos2.setImageResource(j.getImg())
                            txtNombrePos2.text = j.getNombre()
                            txtPuntajePos2.text = j.getPuntaje().toString()
                            if (bandera) txtNombrePos2.textSize = 18.0F
                        }
                        2 -> {
                            imgPos3.setImageResource(j.getImg())
                            txtNombrePos3.text = j.getNombre()
                            txtPuntajePos3.text = j.getPuntaje().toString()
                            if (bandera) txtNombrePos3.textSize = 18.0F
                        }
                        3 -> {
                            imgPos4.setImageResource(j.getImg())
                            txtNombrePos4.text = j.getNombre()
                            txtPuntajePos4.text = j.getPuntaje().toString()
                            if (bandera) txtNombrePos4.textSize = 18.0F
                        }
                        4 -> {
                            imgPos5.setImageResource(j.getImg())
                            txtNombrePos5.text = j.getNombre()
                            txtPuntajePos5.text = j.getPuntaje().toString()
                            if (bandera) txtNombrePos5.textSize = 18.0F
                        }
                        else -> return
                    }
                    // Aumenta la posicion en el ranking
                    cont++
                    // Se reinicia la bandera en caso de haber sido modificada
                    bandera = false
                    // Rompemos el ciclo de usuarios
                    break
                }
    }

    // Funcion para mostrar mensajes
    private fun mostrarToast(mensaje: String) {
        Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show()
    }
}