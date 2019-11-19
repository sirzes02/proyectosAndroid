package com.example.preguntasyrespuestas.ui.actividades.ingreso

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.preguntasyrespuestas.ui.actividades.inside.InsideActivity
import com.example.preguntasyrespuestas.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.btnRegistrar
import kotlinx.android.synthetic.main.activity_main.etqContrasenia2

@Suppress("DEPRECATION", "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Se obtiene una variable que contiene la informacion de la conectividad del telefono
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Si el telefono tiene red
        if (connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo.isConnected) {
            val prefs = getSharedPreferences("dataSession", 0)
            // Si ya hay un usuario Logeado
            if (prefs.getString("nombreUsuario", null) != null) {
                // Inicio de actividades
                startActivity(Intent(this, InsideActivity::class.java))
                // Finalizar esta actividad
                finish()
            } else {
                // Se escucha el boton de ingresar
                btnIngresar.setOnClickListener {
                    // Obtenemos los valores de el nombre y la contraseña
                    val nombre = etqNombre.text.toString().trim()
                    val pass1 = etqContrasenia2.text.toString()

                    // Si el campo nombre y contraseña tienen datos
                    if (nombre != "" && pass1 != "")
                    // Se usa volley para hacer scrapping en una web que cesta conectada mediante PHP a una base de datos MYSQL
                        Volley.newRequestQueue(this).add(
                            // Damos la direccion de red donde se ubica el servidor y se envian los parametros mediante GET
                            StringRequest("${getString(R.string.urlUsuario)}validarNombreContrasenia.php?nombre=$nombre&contrasenia=$pass1",
                                // Al recibir la respuesta
                                Response.Listener<String> { response ->
                                    // Si la respuesta es 0 entonces es correcta la password y el nombre
                                    if (response.toInt() == 0) {
                                        // Se muestra un mensaje con inicio
                                        mostrarToast("Bienvenido, $nombre")

                                        // Se guarda el nombre del usuario en una preferencia
                                        prefs.edit().putString("nombreUsuario", nombre).apply()

                                        //Se inicia la actividad inside y se termina esta
                                        startActivity(Intent(this, InsideActivity::class.java))
                                        finish()
                                    } else mostrarToast("No hay un usuario con esta contraseña")
                                },
                                // En caso de haber un error en la conexion de Volley
                                Response.ErrorListener { mostrarDialog() })
                        )
                    else mostrarToast("Ingrese datos")

                }

                // Se escucha el boton de registro
                btnRegistrar.setOnClickListener {
                    startActivity(Intent(this, RegistroActivity::class.java))
                }
            }
        } else mostrarDialog()
    }

    // Funcion para imprimir los mensajes y borrar los campos
    private fun mostrarToast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
        etqContrasenia2.setText("")
    }

    private fun mostrarDialog() {
        AlertDialog.Builder(this)
            .setMessage("No se encuentra conexion, intente mas tarde.")
            .setCancelable(false)
            .setPositiveButton("Si") { _, _ ->
                finish()
            }.create().show()
    }
}