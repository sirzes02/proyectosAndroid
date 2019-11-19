package com.example.preguntasyrespuestas.clases

class Usuario {
    private var nombre = ""
    private var puntaje = 0
    private var img = 0

    constructor(nombre: String, puntaje: Int, img: Int) {
        this.nombre = nombre
        this.puntaje = puntaje
        this.img = img
    }

    fun getNombre(): String {
        return this.nombre
    }

    fun getPuntaje(): Int {
        return this.puntaje
    }

    fun getImg(): Int {
        return this.img
    }

}