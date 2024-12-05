package com.example.proyectomoviles.models

import java.io.Serializable

class NodoManga(val manga: Manga) : Serializable {
    var izquierda: NodoManga? = null
    var derecha: NodoManga? = null
}
