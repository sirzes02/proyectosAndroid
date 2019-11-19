package com.example.preguntasyrespuestas.ui.actividades.ingreso

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.preguntasyrespuestas.R
import kotlinx.android.synthetic.main.activity_registro.*

class RegistroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        // Se escucha el boton de registrarse
        btnRegistrar.setOnClickListener {
            // Se obtienen los valores
            val nombre = etqNombre.text.toString().trim()
            val pass1 = etqContrasenia1.text.toString()
            val pass2 = etqContrasenia2.text.toString()

            // Si todos los campos fueron llenados
            if (nombre != "" && pass1 != "" && pass2 != "") {
                // Conexion con Volley
                Volley.newRequestQueue(this).add(
                    StringRequest("${getString(R.string.urlUsuario)}validarNombresIguales.php?nombre=$nombre",
                        // Conexion correcta
                        Response.Listener<String> { response ->
                            // Si no retorna 0 quiere decir que no hay ningun usuario con este nombre
                            if (response.toInt() != 0)
                            // Si las contraseñas de verificacion coinciden
                                if (pass1 == pass2) {
                                    mostrarToast("Usuario creado de manera exitosa", 0)

                                    // Se usa volley para registrarlo en la base de datos
                                    Volley.newRequestQueue(this).add(
                                        StringRequest("${getString(R.string.urlUsuario)}agregarUsuario.php?nombre=$nombre&contrasenia=$pass1",
                                            Response.Listener<String> {},
                                            Response.ErrorListener { mostrarDialog() })
                                    )
                                    finish()
                                } else mostrarToast("Las contraseñas no coinciden", 0)
                            else mostrarToast("Este nombre de usuario ya existe", 1)
                        },
                        Response.ErrorListener { mostrarDialog() })
                )
            } else mostrarToast("Debe llenas todos los campos", 0)
        }
    }

    // Funcion de impresion de mensajes
    private fun mostrarToast(mensaje: String, opc: Byte) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
        // si se manda el parametro 1 que tambien se borre el campo de nombre
        if (opc == 1.toByte())
            etqNombre.setText("")
        etqContrasenia1.setText("")
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
