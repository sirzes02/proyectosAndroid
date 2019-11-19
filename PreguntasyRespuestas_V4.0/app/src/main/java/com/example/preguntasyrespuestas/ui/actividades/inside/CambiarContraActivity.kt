package com.example.preguntasyrespuestas.ui.actividades.inside

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.preguntasyrespuestas.R
import kotlinx.android.synthetic.main.activity_cambiar_contra.*

class CambiarContraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cambiar_contra)

        // Se obtienen las preferencais
        val prefs = getSharedPreferences("dataSession", 0)

        // Se configura la imagen
        findViewById<ImageView>(R.id.imagenUsuCambio).setImageResource(prefs.getInt("img", 0))

        // Se escucha el boton de cambio
        btnCambiarContrasenia.setOnClickListener {
            // Se obtienen valores
            val contVieja = etqContraseniaVieja.text.toString()
            val cont1 = etqContrasenia1.text.toString()
            val cont2 = etqContrasenia2.text.toString()

            val nombre = prefs.getString("nombreUsuario", null)

            // Si se tiene datos
            if (contVieja != "" && cont1 != "" && cont2 != "")
            // Si tene mas de 4 digitos
                if (cont1.length >= 4)
                // Si la contraseña nueva es diferente a la vieja
                    if (cont1 != contVieja && cont2 != contVieja)
                    // Si las contraseñas son iguales
                        if (cont1 == cont2)
                            Volley.newRequestQueue(this).add(
                                // Se llama a volley para validar que la contraseña pertenezca al usuario
                                StringRequest("${getString(R.string.urlUsuario)}validarNombreContrasenia.php?nombre=$nombre&contrasenia=$contVieja",
                                    Response.Listener<String> { response ->
                                        // Si se responde verdadero
                                        if (response.toInt() == 0) {
                                            // Se llama a volley para actualizar la contraseña
                                            Volley.newRequestQueue(this).add(
                                                StringRequest("${getString(R.string.urlUsuario)}cambio.php?nombre=$nombre&dato=$cont1&atributo=1",
                                                    Response.Listener<String> {},
                                                    Response.ErrorListener { mostrarDialog() })
                                            )
                                            finish()
                                            mostrarToast("Contraseña actualizada con exito")
                                        } else mostrarToast("No hay un usuario con esta contraseña")
                                    },
                                    Response.ErrorListener { mostrarDialog() })
                            )
                        else mostrarToast("Las contraseñas no coinciden")
                    else mostrarToast("Las contraseña debe ser diferente a la actual")
                else mostrarToast("La contraseña debe tener mas de 4 caracteres")
            else mostrarToast("Ingrese los datos")
        }

        btnCancelarCambioContrasenia.setOnClickListener { finish() }
    }

    // Funcion para presentar mensajes y borrar secciones
    private fun mostrarToast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
        etqContraseniaVieja.setText("")
        etqContrasenia2.setText("")
        etqContrasenia1.setText("")
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
