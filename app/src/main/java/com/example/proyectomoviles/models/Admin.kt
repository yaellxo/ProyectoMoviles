package com.example.proyectomoviles.models

data class Admin(
    val alias: String,
    val clave: String,
    val nombre: String,
    val correo: String,
    val area: String,
    val nivelAcceso: String,
    val adminId: String
)