package com.example.preguntasyrespuestas.ui.actividades.inside

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.preguntasyrespuestas.R
import kotlinx.android.synthetic.main.activity_cambiar_imagen.*

class CambiarImagenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cambiar_imagen)

        // Se obtiene el nombre de las preferencias
        val nombre = getSharedPreferences("dataSession", 0).getString("nombreUsuario", null)

        // Se escuchan todos los botones con sus respectivos datos
        a1.setOnClickListener { cambio(R.drawable.a1, nombre) }
        a2.setOnClickListener { cambio(R.drawable.a2, nombre) }
        a3.setOnClickListener { cambio(R.drawable.a3, nombre) }
        a4.setOnClickListener { cambio(R.drawable.a4, nombre) }
        a5.setOnClickListener { cambio(R.drawable.a5, nombre) }
        a6.setOnClickListener { cambio(R.drawable.a6, nombre) }
        a7.setOnClickListener { cambio(R.drawable.a7, nombre) }
        a8.setOnClickListener { cambio(R.drawable.a8, nombre) }
        a9.setOnClickListener { cambio(R.drawable.a9, nombre) }
        a10.setOnClickListener { cambio(R.drawable.a10, nombre) }
        a11.setOnClickListener { cambio(R.drawable.a11, nombre) }
        a12.setOnClickListener { cambio(R.drawable.a12, nombre) }
        a13.setOnClickListener { cambio(R.drawable.a13, nombre) }
        a14.setOnClickListener { cambio(R.drawable.a14, nombre) }
        a15.setOnClickListener { cambio(R.drawable.a15, nombre) }
        a16.setOnClickListener { cambio(R.drawable.a16, nombre) }

        btnCancelarImagen.setOnClickListener { finish() }
    }

    // Funcion donde se realiza el cambio de imagen
    private fun cambio(img: Int, nombre: String?) {
        // Se llama a volley para enviar el nuevo dato
        Volley.newRequestQueue(this).add(
            StringRequest("${getString(R.string.urlUsuario)}cambio.php?nombre=$nombre&dato=$img&atributo=3",
                Response.Listener<String> {},
                Response.ErrorListener {
                    AlertDialog.Builder(this)
                        .setMessage("No se encuentra conexion, intente mas tarde.")
                        .setCancelable(false)
                        .setPositiveButton("Si") { _, _ ->
                            finish()
                        }.create().show()
                })
        )
        // Se agrega la nueva imagen en las preferencias
        getSharedPreferences("dataSession", 0).edit().putInt("img", img).apply()

        // Se retorna la menu
        startActivity(Intent(this, InsideActivity::class.java))

        // Se actualiza todo el menu
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            super.finishAffinity()
    }


}
