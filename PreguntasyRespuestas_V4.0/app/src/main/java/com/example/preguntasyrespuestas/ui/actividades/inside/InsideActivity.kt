package com.example.preguntasyrespuestas.ui.actividades.inside

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.preguntasyrespuestas.R

class InsideActivity : AppCompatActivity() {

    /*
    * Se trata de conectar lo menos posible la base de datos, esto mediante los SharedPreferences,se
    * trata de alamcenar todo en ellos, para no saturar las conexiones de volley
    */

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inside)

        //SE OBTIENE EL NOMBRE GUARDADO EN EL SHAREDPREFERENCES
        val prefs = getSharedPreferences("dataSession", 0)
        val nombre = prefs.getString("nombreUsuario", null)

        Toast.makeText(this, "Bienvenido, $nombre", Toast.LENGTH_LONG).show()

        //SE OBTIENE EL INGRESO AL MENU
        val navigationView = findViewById<NavigationView>(R.id.nav_view)

        //SE PONE EL NOMBRE DEL USUARIO
        navigationView.getHeaderView(0).findViewById<TextView>(R.id.etqNombreMenu).text = nombre

        //SE PONE EL PUNTAJE DEL USUARIO
        val puntaje = prefs?.getInt("puntaje", -1)

        // Si el puntaje en -1 quiere decir que no hay ningun puntaje guardado
        if (puntaje == -1)
        // se llama a volley para obtener el puntaje
            Volley.newRequestQueue(this).add(
                StringRequest("${getString(R.string.urlUsuario)}getPuntaje.php?nombre=$nombre",
                    Response.Listener<String> { response ->
                        // Se guarda la nueva configuracion en sharedPreferences
                        prefs.edit().putInt("puntaje", response.toInt()).apply()
                        // Se actualiza el puntaje en la interfaz
                        navigationView.getHeaderView(0).findViewById<TextView>(R.id.etqPuntajeMenu)
                            .text = "Puntaje: $response"
                    },
                    Response.ErrorListener { mostrarDialog() })
            )
        else navigationView.getHeaderView(0).findViewById<TextView>(R.id.etqPuntajeMenu).text =
            "Puntaje: $puntaje"

        // SE OBTIENE LA LA ETIQUETA DONDE SE PONDRA LA IMAGEN
        val imageMenu = navigationView.getHeaderView(0).findViewById<ImageView>(R.id.imagenUsu)
        // SE SETEA LA IMAGEN SEGUN LA IMAGEN DEL USUARIO
        val img = prefs?.getInt("img", -1)

        // Si imagen el -1 quiere decir que no hay ninguna guardada
        if (img == -1)
        // se llama Volley para obteneer la imagen
            Volley.newRequestQueue(this).add(
                StringRequest("${getString(R.string.urlUsuario)}getImage.php?nombre=$nombre",
                    Response.Listener<String> { response ->
                        // Se actualiza la imagen
                        imageMenu.setImageResource(response.toInt())
                        // Se actualiza la nueva imagen en las preferencias
                        prefs.edit().putInt("img", response.toInt()).apply()
                    },
                    Response.ErrorListener { mostrarDialog() })
            )
        else imageMenu.setImageResource(img!!)

        setSupportActionBar(findViewById(R.id.toolbar))

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navView = findViewById<NavigationView>(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_inicio,
                R.id.nav_ranking,
                R.id.nav_cuenta
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_inside, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
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
