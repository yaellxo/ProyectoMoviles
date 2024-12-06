package com.example.proyectomoviles.models
import android.util.Log
import java.io.Serializable

class ArbolBinarioManga : Serializable {
    private var raiz: NodoManga? = null

    // Agrega un manga al árbol solo si no existe un manga con el mismo ID
    fun agregarManga(manga: Manga) {
        if (!contieneManga(manga)) { // Verifica si el manga ya existe en el árbol
            Log.d("ArbolBinarioManga", "Agregando manga con ID: ${manga.mangaId}, Título: ${manga.titulo}")
            raiz = agregarMangaRecursivo(raiz, manga)
        } else {
            Log.d("ArbolBinarioManga", "El manga con ID ${manga.mangaId} ya existe en el árbol: ${manga.titulo}")
        }
    }

    // Inserción recursiva en el árbol binario de búsqueda, utilizando ID para la comparación
    private fun agregarMangaRecursivo(nodo: NodoManga?, manga: Manga): NodoManga {
        if (nodo == null) {
            Log.d("ArbolBinarioManga", "Creando nuevo nodo para manga con ID: ${manga.mangaId}")
            return NodoManga(manga)
        }

        // Compara por ID, no por título
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

    // Devuelve una lista de mangas en orden (sin duplicados por ID)
    fun obtenerMangasEnOrden(): List<Manga> {
        val listaMangas = mutableListOf<Manga>()
        inOrden(raiz, listaMangas)
        return listaMangas
    }

    // Recorrido en orden del árbol binario
    private fun inOrden(nodo: NodoManga?, lista: MutableList<Manga>) {
        if (nodo != null) {
            inOrden(nodo.izquierda, lista)  // Recorre el subárbol izquierdo
            lista.add(nodo.manga)           // Añade el manga actual a la lista
            inOrden(nodo.derecha, lista)    // Recorre el subárbol derecho
        }
    }

    // Verifica si un manga con el mismo ID ya existe en el árbol
    fun contieneManga(manga: Manga): Boolean {
        return contieneMangaRecursivo(raiz, manga)
    }

    // Búsqueda recursiva por ID del manga
    private fun contieneMangaRecursivo(nodo: NodoManga?, manga: Manga): Boolean {
        if (nodo == null) {
            return false
        }

        // Compara por ID, no por título
        return when {
            manga.mangaId < nodo.manga.mangaId -> contieneMangaRecursivo(nodo.izquierda, manga)
            manga.mangaId > nodo.manga.mangaId -> contieneMangaRecursivo(nodo.derecha, manga)
            else -> true  // Si los IDs son iguales, significa que el manga ya existe
        }
    }

    // Limpia el árbol
    fun clear() {
        raiz = null
    }
}
