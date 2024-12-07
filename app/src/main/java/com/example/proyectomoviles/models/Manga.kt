package com.example.proyectomoviles.models

import java.io.Serializable

data class Manga(
    val titulo: String,
    val precio: Float,
    val stock: Int,
    val descripcion: String,
    val volumen: Double,
    val autor: String,
    val genero: String,
    val editorial: String,
    val publicacion: String,
    val imagenUrl: String,
    val mangaId: String
) : Serializable
