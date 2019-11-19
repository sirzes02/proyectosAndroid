package com.example.preguntasyrespuestas.clases

class Pregunta {
    private var titulo = ""
    private var respuestas = ""
    private var verdadera = ""

    // Clase donde se almacenan las preguntas leidas de la base de datos
    constructor(titulo: String, respuestas: String, verdadera: String) {
        this.titulo = titulo
        this.respuestas = respuestas
        this.verdadera = verdadera
    }

    fun getTitulo(): String {
        return this.titulo
    }

    fun getRespuesta(): String {
        return this.respuestas
    }

    fun getVerdadera(): String {
        return this.verdadera
    }

}