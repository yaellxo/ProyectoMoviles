package com.example.proyectomoviles.models
import android.util.Log
import java.io.Serializable

class ArbolBinarioManga : Serializable {
    var raiz: NodoManga? = null

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

    fun eliminarManga(mangaId: String): Boolean {
        Log.d("ArbolBinarioManga", "Eliminando manga con ID: $mangaId")

        if (raiz != null && raiz!!.manga.mangaId == mangaId) {
            Log.d("ArbolBinarioManga", "La raíz es el nodo a eliminar")
            raiz = eliminarRaiz(raiz)
            return raiz == null
        }

        val mangaEliminado = eliminarMangaRecursivo(raiz, mangaId) != null

        if (mangaEliminado) {
            Log.d("ArbolBinarioManga", "Manga eliminado con éxito.")
        }

        val mangasEnOrden = obtenerMangasEnOrden()
        Log.d("ArbolBinarioManga", "Árbol después de la eliminación (en orden): ${mangasEnOrden.joinToString()}")

        return mangaEliminado
    }

    private fun eliminarRaiz(nodo: NodoManga?): NodoManga? {
        if (nodo == null) return null

        if (nodo.izquierda == null) {
            return nodo.derecha
        }
        if (nodo.derecha == null) {
            return nodo.izquierda
        }

        val sucesor = obtenerMinimo(nodo.derecha)

        nodo.manga = sucesor.manga

        nodo.derecha = eliminarMangaRecursivo(nodo.derecha, sucesor.manga.mangaId)

        return nodo
    }


    private fun eliminarMangaRecursivo(nodo: NodoManga?, mangaId: String): NodoManga? {
        if (nodo == null) return null

        when {
            mangaId < nodo.manga.mangaId -> {
                nodo.izquierda = eliminarMangaRecursivo(nodo.izquierda, mangaId)
            }
            mangaId > nodo.manga.mangaId -> {
                nodo.derecha = eliminarMangaRecursivo(nodo.derecha, mangaId)
            }
            else -> {
                Log.d("ArbolBinarioManga", "Manga encontrado, eliminando nodo con ID: $mangaId")
                if (nodo.izquierda == null && nodo.derecha == null) {
                    return null
                }
                if (nodo.izquierda == null) {
                    return nodo.derecha
                }
                if (nodo.derecha == null) {
                    return nodo.izquierda
                }
                val sucesor = obtenerMinimo(nodo.derecha)
                Log.d("ArbolBinarioManga", "Nodo con dos hijos, reemplazado por el sucesor con ID: ${sucesor.manga.mangaId}")
                nodo.manga = sucesor.manga
                nodo.derecha = eliminarMangaRecursivo(nodo.derecha, sucesor.manga.mangaId)
            }
        }

        return nodo
    }

    fun obtenerMinimo(nodo: NodoManga?): NodoManga {
        var actual = nodo
        while (actual?.izquierda != null) {
            actual = actual.izquierda
        }
        return actual!!
    }

    fun modificarMangaPorId(id: String, nuevoManga: Manga): Boolean {
        val nodo = buscarNodoPorId(raiz, id)
        return if (nodo != null) {
            nodo.manga = nuevoManga
            Log.d("ArbolBinarioManga", "Manga con ID $id modificado exitosamente.")

            val mangasEnOrden = obtenerMangasEnOrden()
            Log.d("ArbolBinarioManga", "Árbol después de la modificación (en orden): ${mangasEnOrden.joinToString()}")

            true
        } else {
            false
        }
    }

    private fun buscarNodoPorId(nodo: NodoManga?, id: String): NodoManga? {
        if (nodo == null) return null
        return when {
            id < nodo.manga.mangaId -> buscarNodoPorId(nodo.izquierda, id)
            id > nodo.manga.mangaId -> buscarNodoPorId(nodo.derecha, id)
            else -> nodo
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

    fun actualizarMangas(mangasActualizadas: List<Manga>): Boolean {
        raiz = null

        mangasActualizadas.forEach { manga ->
            agregarManga(manga)
        }

        val mangasEnOrden = obtenerMangasEnOrden()
        Log.d("ArbolBinarioManga", "Árbol después de la actualización (en orden): ${mangasEnOrden.joinToString()}")

        return mangasEnOrden.size == mangasActualizadas.size

    }
}