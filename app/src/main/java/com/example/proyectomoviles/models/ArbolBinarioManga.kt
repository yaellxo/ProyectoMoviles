package com.example.proyectomoviles.models
import android.util.Log
import java.io.Serializable

class ArbolBinarioManga : Serializable {
    private var raiz: NodoManga? = null

    fun agregarManga(manga: Manga) {
        if (!contieneManga(manga)) {
            Log.d("ArbolBinarioManga", "Agregando manga con ID: ${manga.mangaId}, Título: ${manga.titulo}")
            raiz = agregarMangaRecursivo(raiz, manga)
        } else {
            Log.d("ArbolBinarioManga", "El manga con ID ${manga.mangaId} ya existe en el árbol: ${manga.titulo}")
        }
    }

    private fun agregarMangaRecursivo(nodo: NodoManga?, manga: Manga): NodoManga {
        if (nodo == null) {
            Log.d("ArbolBinarioManga", "Creando nuevo nodo para manga con ID: ${manga.mangaId}")
            return NodoManga(manga)
        }

        return when {
            manga.mangaId < nodo.manga.mangaId -> {
                Log.d("ArbolBinarioManga", "ID ${manga.mangaId} es menor que ID ${nodo.manga.mangaId}, yendo a la izquierda")
                nodo.izquierda = agregarMangaRecursivo(nodo.izquierda, manga)
                nodo
            }
            manga.mangaId > nodo.manga.mangaId -> {
                Log.d("ArbolBinarioManga", "ID ${manga.mangaId} es mayor que ID ${nodo.manga.mangaId}, yendo a la derecha")
                nodo.derecha = agregarMangaRecursivo(nodo.derecha, manga)
                nodo
            }
            else -> {
                Log.d("ArbolBinarioManga", "El manga con ID ${manga.mangaId} ya existe en el árbol")
                nodo
            }
        }
    }

    fun obtenerMangasEnOrden(): List<Manga> {
        val listaMangas = mutableListOf<Manga>()
        inOrden(raiz, listaMangas)
        return listaMangas
    }

    private fun inOrden(nodo: NodoManga?, lista: MutableList<Manga>) {
        if (nodo != null) {
            inOrden(nodo.izquierda, lista)
            lista.add(nodo.manga)
            inOrden(nodo.derecha, lista)
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
            manga.mangaId < nodo.manga.mangaId -> contieneMangaRecursivo(nodo.izquierda, manga)
            manga.mangaId > nodo.manga.mangaId -> contieneMangaRecursivo(nodo.derecha, manga)
            else -> true
        }
    }

    fun clear() {
        raiz = null
    }
}