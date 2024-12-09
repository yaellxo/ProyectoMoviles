package com.example.proyectomoviles.models

import java.io.Serializable

data class Usuario(
    val userId: String,
    val nombre: String,
    val alias: String,
    val correo: String,
    val edad: String,
    val clave: String,
    val userPhotoUri: String = "",
    var carrito: MutableList<Manga> = mutableListOf()
) : Serializable
