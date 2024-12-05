package com.example.proyectomoviles.models

import java.io.Serializable

data class Evento(
    val nombre: String,
    val fecha: String,
    val ubicacion: String,
    val descripcion: String
) : Serializable