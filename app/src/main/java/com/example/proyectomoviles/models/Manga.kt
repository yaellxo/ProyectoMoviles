package com.example.proyectomoviles.models

import java.io.Serializable

data class Manga(
    var titulo: String,
    var precio: Float,
    var stock: Int,
    var descripcion: String,
    var volumen: Double,
    var autor: String,
    var genero: String,
    var editorial: String,
    var publicacion: String,
    var imagenUrl: String,
    var mangaId: String
) : Serializable