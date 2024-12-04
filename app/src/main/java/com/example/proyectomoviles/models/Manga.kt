package com.example.proyectomoviles.models

data class Manga(
    val id: String,
    var nombre: String,
    var precio: Float,
    var stock: Int,
    var descripcion: String,
    var volumen: Int,
    var autor: String,
    var genero: String,
    var editorial: String,
    var publicacion: String,
    var imagen: String
)