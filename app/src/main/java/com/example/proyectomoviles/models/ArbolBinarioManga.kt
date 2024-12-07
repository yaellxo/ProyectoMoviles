package com.example.proyectomoviles.models
import android.util.Log
import java.io.Serializable

class ArbolBinarioManga : Serializable {
    private var raiz: NodoManga? = null

    fun agregarManga(manga: Manga) {
        if (!contieneManga(manga)) { // Verifica si el manga ya existe en el árbol
            raiz = agregarMangaRecursivo(raiz, manga)
        } else {
            Log.d("ArbolBinarioManga", "El manga ya existe en el árbol: ${manga.titulo}")
        }
    }

    private fun agregarMangaRecursivo(nodo: NodoManga?, manga: Manga): NodoManga {
        if (nodo == null) {
            return NodoManga(manga)
        }

        if (manga.titulo < nodo.manga.titulo) {
            nodo.izquierda = agregarMangaRecursivo(nodo.izquierda, manga)
        }
        else if (manga.titulo > nodo.manga.titulo) {
            nodo.derecha = agregarMangaRecursivo(nodo.derecha, manga)
        }

        return nodo
    }

    fun obtenerMangasEnOrden(): List<Manga> {
        val mangas = mutableListOf<Manga>()
        recorrerEnOrden(raiz, mangas)
        return mangas
    }

    private fun recorrerEnOrden(nodo: NodoManga?, mangas: MutableList<Manga>) {
        if (nodo != null) {
            recorrerEnOrden(nodo.izquierda, mangas)
            mangas.add(nodo.manga)
            recorrerEnOrden(nodo.derecha, mangas)
        }
    }

    fun contieneManga(manga: Manga): Boolean {
        return contieneMangaRecursivo(raiz, manga)
    }

    private fun contieneMangaRecursivo(nodo: NodoManga?, manga: Manga): Boolean {
        if (nodo == null) {
            return false
        }
        return when {
            manga.titulo < nodo.manga.titulo -> contieneMangaRecursivo(nodo.izquierda, manga)
            manga.titulo > nodo.manga.titulo -> contieneMangaRecursivo(nodo.derecha, manga)
            else -> true
        }
    }

    fun clear() {
        raiz = null
    }

    fun addAll(otroArbol: ArbolBinarioManga) {
        val mangas = otroArbol.obtenerMangasEnOrden()
        for (manga in mangas) {
            agregarManga(manga)
        }
    }
}