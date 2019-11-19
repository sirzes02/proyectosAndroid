package com.example.preguntasyrespuestas.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.preguntasyrespuestas.R
import com.example.preguntasyrespuestas.ui.actividades.ingreso.MainActivity
import com.example.preguntasyrespuestas.ui.actividades.inside.CambiarContraActivity
import com.example.preguntasyrespuestas.ui.actividades.inside.CambiarImagenActivity

class CuentaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Se obtiene la vista ya que este es un fragment
        val vista = inflater.inflate(R.layout.fragment_cuenta, container, false)
        // Se obtienen las preferencias
        val prefs = activity?.getSharedPreferences("dataSession", 0)

        // Se le da valor a la imagen
        vista.findViewById<ImageView>(R.id.imagenUsuCue).setImageResource(prefs!!.getInt("img", 0))

        // Se escucha el boton de cerrar la sesion
        vista.findViewById<Button>(R.id.btnCerrarSesion).setOnClickListener {
            // Se editan los preferences para volverlas negativas significando que no hay un usuario guardado
            prefs.edit()?.putString("nombreUsuario", null)?.apply()
            prefs.edit()?.putInt("puntaje", -1)?.apply()
            prefs.edit()?.putInt("img", -1)?.apply()
            // Se inicia la actividad del login
            startActivity(Intent(activity, MainActivity::class.java))
            activity?.finish()
        }

        // Se llama a cambiar la contraseña
        vista.findViewById<Button>(R.id.btnCambiarContrasenia).setOnClickListener {
            startActivity(Intent(activity, CambiarContraActivity::class.java))
        }

        // Se llama a cambiar la imagen
        vista.findViewById<Button>(R.id.btnCambiarImg).setOnClickListener {
            startActivity(Intent(activity, CambiarImagenActivity::class.java))
        }

        return vista
    }
}